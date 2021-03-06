(function(factory) {
  if (typeof define === 'function' && define.amd) {
    // AMD. Register as an anonymous module.
    define(['jquery'], factory);
  } else if (typeof module === 'object' && module.exports) {
    // Node/CommonJS
    module.exports = function(root, jQuery) {
      if (jQuery === undefined) {
        // require('jQuery') returns a factory that requires window to
        // build a jQuery instance, we normalize how we use modules
        // that require this pattern but the window provided is a noop
        // if it's defined (how jquery works)
        if (typeof window !== 'undefined') {
          jQuery = require('jquery');
        } else {
          jQuery = require('jquery')(root);
        }
      }
      factory(jQuery);
      return jQuery;
    };
  } else {
    // Browser globals
    factory(jQuery);
  }
}(function($) {
  /***
   * collapse menu eg.
   * @param opts
   */
  $.fn.collapse = function(opts) {
    console.log(' log: ' + opts);
    // 如果是容器触发,就绑定事件到容器,否则绑定到容器内的元素上
    let paras = $.extend({
      triggerCell: 'h3.mdc-list-group__subheader',
      triggerContainer: '.mdc-tree-heading',
      trigger: 'click',
      toggleCell: 'ul',
      expandAll: true, // 是否全部展开
      containerTrigger: true, // 是否容器触发
      toggleEvt: function() {
      },
    }, opts || {});
    let _triggerCell = paras.triggerCell;
    let _trigger = paras.trigger;
    let _toggleCell = paras.toggleCell;
    let _expandAll = paras.expandAll;
    let _toggleEvt = paras.toggleEvt;
    console.log(' log: ' + _triggerCell, _toggleCell);
    $(this).find(_triggerCell).on(_trigger, function() {
      _toggleEvt && _toggleEvt(this);
      $(this).next(_toggleCell).slideToggle(150);
    });
  };

  /***
   * ===================================================
   *
   * 基于谷歌Material-Design样式的动态数据表格.
   * @version v1.0.0
   * @author Tang Xiaofeng <feng1390635973@gmail.com>
   * https://github.com/ramerf/mdc-data-table.git
   *
   * ===================================================
   */
  class MDCDataTable {
    constructor(data = [], queryParams = {}, container, handler) {
      this.data = data;
      this.container = container;
      this.queryParams = queryParams;
      this.handler = handler;
    }

    /**获取已选择项*/
    getSelectItems() {
      let selectItems = [];
      let ts = this;
      $.each(
          $(this.container).find('tbody tr td:first-of-type div.mdc-checkbox'),
          function(index, item) {
            if (new mdc.checkbox.MDCCheckbox(item).checked) {
              selectItems.push(ts.data[index]);
            }
          });
      console.debug(selectItems);
      return selectItems;
    }

    /**
     * 刷新表格
     * @param queryParams 查询参数
     * @param delCount 删除数目,用于执行删除操作重新计算页数
     */
    refresh(queryParams, delCount = 0) {
      let totalElements = this.queryParams.totalElements - delCount;
      let page = queryParams && queryParams.page
          ? queryParams.page
          : this.queryParams.page;
      let size = queryParams && queryParams.size
          ? queryParams.size
          : this.queryParams.size;
      // 删除元素: 第一页删除页号不变,否则判断当前页号是否大于删除后的数据总页号,更新页号
      // 删除元素后,点击刷新,有问题,内部页码没有更新
      let fixTotalPage = totalElements % size === 0
          ? ((totalElements / size) >>> 0)
          : ((totalElements / size) >>> 0) + 1;
      this.queryParams.page = page > fixTotalPage ? fixTotalPage : page;
      $.extend(this.queryParams, queryParams);
      this.handler.pullData(this.queryParams, true);
    }

    fixBtnColor() {
      this.handler.colorPageBtn('#pageBtn' + this.queryParams.page);
    }
  }

  /**
   * 基于谷歌Material-Design样式的动态数据表格.
   * https://github.com/ramerf/mdc-data-table.git
   * @param  opts
   *  {<br>
   *      url, 请求地址<br>
   *      queryParams, 查询参数<br>
   *      container, table父容器<br>
   *      columns:[{<br>
   *          data: the propId, 字段名<br>
   *          editable: enable edit, 是否可编辑<br>
   *          render: the data render. 额外处理函数<br>
   *      }], 表格列<br>
   *      thead: [{<br>
   *          data: the thead content, 表头<br>
   *          width: the thead width. 宽度<br>
   *      }],<br>
   *      dataHandler: res=> {return {<br>
                 // 请求结果: true/false<br>
                 result: res.result,<br>
                 // 数据<br>
                 content: res.data.content,<br>
                 // 总页数<br>
                 totalPages: res.data.totalPages,<br>
                 // 总记录数<br>
                 totalElements: res.data.totalElements,<br>
                 // 当前页号,从1开始<br>
                 page: Number(res.data.number + 1),<br>
                 // 每页记录数<br>
                 size: res.data.size<br>
                 };<br>
   *      success, 成功回调<br>
   *      error, 失败回调<br>
   *      ...<br>
   *  }
   *
   * @see https://github.com/material-components/material-components-web.git
   */
  $.fn.mdcDataTable = function(opts) {
    let paras = $.extend({
      url: null,
      queryParams: {},
      container: null,
      type: 'GET',
      dataType: 'json',
      contentType: 'application/x-www-form-urlencoded',
      // container: '#mdc-data-table',
      columns: [
        /*{
         // 字段名
         data: 'name',
         // 重新渲染值: val: 当前值,data: 所有数据
         render: (val,data)=>{},
         // 是否可编辑
         editable: true/false
         }*/
      ],
      check: true,
      thead: [
        /*{
         // 表头名称
         data: '表头',
         // 自定义样式: 默认数字样式,右贴靠,添加mdc-data-table__cell--non-numeric表示非数字
         style: 'mdc-data-table__cell--non-numeric'
         // 列宽
         width: '60px'
         },*/
      ],
      pageSize: 2,
      // 设置页面可以显示的数据条数
      pageList: [5, 10, 15, 20],
      // 首页页码
      pageNumber: 1,
      editable: false,
      // 自定义数据处理
      dataHandler: res => {
        /*return {
         // 请求结果: true/false
         result: res.result,
         data: {
         // 数据
         content: res.data.content,
         // 总页数
         totalPages: res.data.totalPages,
         // 总记录数
         totalElements: res.data.totalElements,
         // 当前页号,从1开始
         page: Number(res.data.number + 1),
         // 每页记录数
         size: res.data.size
         }
         };*/
      },
      // 未选事件
      noneSelect: undefined,
      // 单选事件
      singleSelect: undefined,
      // 多选事件
      multipleSelect: undefined,
      // 行点击事件,入参(row(当前行数据),data(所有行数据),当前行索引;从1开始)
      rowClick: undefined,
      // 初始化完成后回调
      onInitFinish: () => {
      },
      // 每次加载成功后回调
      onLoadSuccess: () => {
      },
      // 每次加载失败后回调
      onLoadError: () => {
      },
    }, opts || {});
    let _url = paras.url;
    let _contentType = paras.contentType;
    /** 请求方式 */
    let _type = paras.type;
    /** 查询条件 */
    let _queryParams = paras.queryParams;
    /** table父容器 */
    let _container = paras.container ? $(paras.container) : this;
    let _columns = paras.columns;
    let _check = paras.check;
    let _thead = paras.thead;
    let _pageSize = paras.pageSize;
    // 可供下拉选择的每页大小,暂未实现
    let _pageList = paras.pageList;
    let _pageNumber = paras.pageNumber;
    let _dataHandler = paras.dataHandler;
    let _noneSelect = paras.noneSelect;
    let _singleSelect = paras.singleSelect;
    let _multipleSelect = paras.multipleSelect;
    let _rowClick = paras.rowClick;
    let _initFinishCallback = paras.onInitFinish;
    let _successCallback = paras.onLoadSuccess;
    let _errorCallback = paras.onLoadError;
    // 是否首次初始化
    let initCallback = true;
    if (_url == null) {
      console.error('请求地址不能为空');
      return false;
    }
    let tableNode = $(
        '<table class="mdc-data-table mdc-shadow--2dp"></table>');
    let theadNode = $('<thead></thead>');
    let tbodyNode = $('<tbody></tbody>');
    let tfootNode = $('<tfoot></tfoot>');
    let thTrNode = $('<tr></tr>');
    let checkNodeTh = $('<th class="mdc-data-table__cell--non-numeric"></th>');
    let pageContainer = $('<div class="pagination-container"></div>');
    let pageCountCode = $(`<p class="count static-info"></p>`);
    let pageBtnContainer = $('<ul></ul>');
    pageContainer.append(pageCountCode).append(pageBtnContainer);
    const checkNodeStr =
        '<div class="mdc-form-field"><div class="mdc-checkbox"><input type="checkbox" class="mdc-checkbox__native-control"/><div class="mdc-checkbox__background"><svg class="mdc-checkbox__checkmark" viewBox="0 0 24 24"><path class="mdc-checkbox__checkmark-path" fill="none" d="M1.73,12.91 8.1,19.28 22.79,4.59"></path></svg><div class="mdc-checkbox__mixedmark"></div></div></div></div>';
    if (_check) {
      checkNodeTh.append($(checkNodeStr));
      bindCheckAllEvt(checkNodeTh.find('input[type=checkbox]'));
      thTrNode.append(checkNodeTh);
    }
    _thead.forEach(function(item) {
      let thNode;
      if (item.width) {
        thNode = $(`<th width="${item.width}">${item.data}</th>`);
      } else {
        thNode = $(`<th>${item.data}</th>`);
      }
      if (item.style) {
        thNode.addClass(item.style);
      }
      thTrNode.append(thNode);
    });
    theadNode.append(thTrNode);
    //
    tableNode.append(theadNode);
    tableNode.append(tbodyNode);
    tableNode.append(tfootNode);
    _container.append(tableNode).append(pageContainer);
    /** 服务器数据(数组),不包含分页信息 */
    let dataList = [];
    /** 分页信息,包含page,size */
    let pageInfo = {'page': _pageNumber, 'size': _pageSize};
    $.extend(_queryParams, pageInfo);
    pullData(_queryParams, true);
    let mdcDataTable = new MDCDataTable(dataList, _queryParams, _container,
        new Handler());

    /** 拉取服务器数据 */
    function pullData(queryParams, refreshBtn = false) {
      console.debug(queryParams);
      $.ajax({
        url: _url,
        type: _type,
        data: queryParams,
        contentType: _contentType,
        success: function(res) {
          $.extend(res.data, _dataHandler(res) || {});
          if ((res.data.result ? res.data.result : res.result) !== true) {
            console.error('拉取数据失败 !');
            return false;
          }
          // 总页数
          pageInfo.totalPages = res.data.totalPages;
          // 总记录数
          pageInfo.totalElements = res.data.totalElements;
          // 当前页号,从1开始
          pageInfo.page = Number(res.data.number + 1);
          // 每页记录数
          pageInfo.size = res.data.size;
          // 数据内容
          dataList = res.data.content;
          mdcDataTable.data = dataList;
          console.debug(dataList);
          appendData(pageInfo, refreshBtn);
        },
        error: function() {
          console.error('拉取数据失败 !');
          _errorCallback();
        },
      });
    }

    /**
     * 拼接数据.
     * @param _pageInfo 分页信息{page:'页号,从1开始',size:'页数'}
     * @param refreshBtn 是否初始化按钮
     */
    function appendData(
        _pageInfo = {'page': _pageNumber, 'size': _pageSize},
        refreshBtn = false) {
      appendBodyData(dataList);
      let from = `${((_pageInfo.page - 1) * _pageInfo.size + 1)}`;
      let to = `${(_pageInfo.totalElements % _pageInfo.size !== 0
          && _pageInfo.page === _pageInfo.totalPages)
          ? _pageInfo.totalElements
          : _pageInfo.page * _pageInfo.size}`;
      let total = `${_pageInfo.totalElements}`;
      let size = `${_pageInfo.size}`;
      let staticInfo = `显示第 ${from} 到第 ${to} 条记录，总共 ${total} 条记录 每页显示 ${size} 条记录`;
      if (refreshBtn) {
        initNumberBtn(pageContainer);
      }
      _container.
          find('.pagination-container p.static-info').
          text(staticInfo);
      updateNumberBtn(_pageInfo.page, _pageInfo.totalPages);
      // 上一页,下一页点击事件
      _container.find('button.prev-btn,button.next-btn').unbind();
      _container.find('button.prev-btn,button.next-btn').
          on('click', function() {
            pageInfo.page = $(this).hasClass('prev-btn')
                ? --_pageInfo.page
                : ++_pageInfo.page;
            updateNumberBtn(_pageInfo.page, _pageInfo.totalPages);
            pullData($.extend(_queryParams, _pageInfo), true);
          });
      // 页号点击事件
      _container.find('button[class*="num-btn"]').unbind();
      _container.find('button[class*="num-btn"]').on('click', function() {
        let numberClick = Number($(this).text());
        if ($(this).hasClass('mdc-button--raised') || isNaN(numberClick)) {
          return false;
        }
        _queryParams.page = _pageInfo.page = numberClick;
        updateNumberBtn(_pageInfo.page, _pageInfo.totalPages);
        pullData($.extend(_queryParams, _pageInfo), true);
      });
      // 每次加载成功后回调
      _successCallback();
      // 初始化完成后回调
      if (initCallback) {
        _initFinishCallback();
        initCallback = false;
      }
    }

    /** 初始化页码按钮 */
    function initNumberBtn() {
      $(pageBtnContainer).empty();
      let prevBtn = $(
          '<li><button class="mdc-icon-button material-icons mdc-ripple-upgraded prev-btn">keyboard_arrow_left</button></li>');
      $(pageBtnContainer).append(prevBtn);
      if (pageInfo.totalPages > 7) {
        for (let i = 1; i < 8; i++) {
          let li = $('<li></li>');
          let btn;
          if (i === 6) {
            btn = $(`<button class="mdc-button mdc-ripple-upgraded num-btn${i}">
                        <span class="mdc-fab__label">...</span>
                     </button>`);
          } else if (i === 7) {
            btn = $(`<button class="mdc-button mdc-ripple-upgraded num-btn${i}">
                        <span class="mdc-fab__label">${pageInfo.totalPages}</span>
                     </button>`);
          } else {
            btn = $(
                `<button class="mdc-button mdc-ripple-upgraded num-btn${i}"><span class="mdc-fab__label">${i}</span></button>`);
          }
          $(li).append(btn);
          $(pageBtnContainer).append(li);
        }
      } else {
        for (let i = 1; i < pageInfo.totalPages + 1; i++) {
          let li = $('<li></li>');
          let btn = $(
              `<button data-mdc-auto-init="MDCButton" class="mdc-button mdc-ripple-upgraded num-btn${i}"><span class="mdc-fab__label">${i}</span></button>`);
          $(li).append(btn);
          $(pageBtnContainer).append(li);
        }
      }
      let nextBtn = $(
          '<li><button class="mdc-icon-button material-icons mdc-ripple-upgraded next-btn">keyboard_arrow_right</button></li>');
      $(pageBtnContainer).append(nextBtn);
    }

    /**
     * 更新页码按钮.
     * @param number 当前页码
     * @param totalPages 总页数
     * @returns {boolean}
     */
    function updateNumberBtn(number, totalPages) {
      // 取消表头选中
      if (_check) {
        new mdc.checkbox.MDCCheckbox(
            $(checkNodeTh.find('input[type=checkbox]')).
                parent('.mdc-checkbox').
                get(0)).checked = false;
      }
      _container.find('.pagination-container ul button.num-btn7').
          text(totalPages);
      let $prevBtn = _container.find(
          '.pagination-container ul button.prev-btn');
      let $nextBtn = _container.find(
          '.pagination-container ul button.next-btn');
      $prevBtn.prop('disabled', false);
      $nextBtn.prop('disabled', false);
      if (number === 1) {
        $prevBtn.prop('disabled', 'disabled');
        colorPageBtn('.pagination-container ul button.num-btn1');
      }
      if (number >= totalPages) {
        $nextBtn.prop('disabled', 'disabled');
        colorPageBtn('.pagination-container ul button.num-btn7');
      }
      if (totalPages <= 7) {
        for (let i = 1; i <= totalPages; i++) {
          _container.find('.pagination-container ul button.num-btn' + i).
              text(i);
          if (i === number) {
            colorPageBtn('.pagination-container ul button.num-btn' + i);
          }
        }
        return false;
      }
      if (number <= 4) {
        for (let i = 2; i < 7; i++) {
          if (i === 6) {
            _container.find('.pagination-container ul button.num-btn' + i).
                text('...');
          } else {
            _container.find('.pagination-container ul button.num-btn' + i).
                text(i);
          }
          if (i === number) {
            colorPageBtn('.pagination-container ul button.num-btn' + number);
          }
        }
      } else if (number > totalPages - 4) {
        let btnNum = 2;
        for (let i = totalPages - 5; i < totalPages; i++) {
          if (btnNum === 2) {
            _container.find('.pagination-container ul button.num-btn' + btnNum).
                text('...');
          } else {
            _container.find('.pagination-container ul button.num-btn' + btnNum).
                text(i);
          }
          if (i === number) {
            colorPageBtn('.pagination-container ul button.num-btn' + btnNum);
          }
          btnNum++;
        }
      } else {
        _container.find('.pagination-container ul button.num-btn2').text('...');
        _container.find('.pagination-container ul button.num-btn3').
            text(number - 1);
        _container.find('.pagination-container ul button.num-btn4').
            text(number);
        _container.find('.pagination-container ul button.num-btn5').
            text(number + 1);
        _container.find('.pagination-container ul button.num-btn6').text('...');
        colorPageBtn('.pagination-container ul button.num-btn4');
      }
    }

    /**
     * 选中页码按钮.
     * @param selector 选择器
     */
    function colorPageBtn(selector) {
      _container.find(selector).
          parents('ul').
          find('button[class*="mdc-button"]').
          removeClass('mdc-button--raised');
      _container.find(selector).
          addClass('mdc-button--raised mdc-button--circle');
    }

    /**
     * 拼接body数据
     * @param data 数据
     */
    function appendBodyData(data) {
      tbodyNode.empty();
      if (data) {
        let rowIndex = 1;
        data.forEach(function(_obj) {
          let tr = $('<tr></tr>');
          // 选中事件
          if (_check) {
            let checkNodeTd = $(
                '<td class="mdc-data-table__cell--non-numeric"></td>');
            checkNodeTd.append($(checkNodeStr));
            tr.append(checkNodeTd);
            bindCheckEvt(checkNodeTd.find('input[type=checkbox]'));
          }
          // 行点击事件
          if (_rowClick) {
            tr.attr('data-index', rowIndex++);
            tr.click(function() {
              _rowClick(_obj, data, tr.attr('data-index'), this);
            });
          }
          _columns.forEach(function(_col, index) {
            let td;
            let editable = _col.editable;
            // 如果值为空则值为-
            let val = _obj[_col.data];
            val = typeof val === 'undefined' || val === null
                ? '-'
                : val;
            if (typeof (_col.render) != 'undefined') {
              td = $(`<td>${_col.render(val, data)}</td>`);
            } else {
              if (typeof (editable) != 'undefined' && editable === true) {
                td = $(
                    `<td><input type="text" value="${val}"/></td>`);
              } else {
                td = $(`<td>${val}</td>`);
              }
            }
            if (_thead[index].style) {
              td.addClass(_thead[index].style);
            }
            tr.append(td);
          });
          tbodyNode.append(tr);
        });
      }

    }

    /**
     * 表头全选
     * @param obj 点选元素
     */
    function bindCheckAllEvt(obj) {
      obj.change(function() {
        const checkbox = new mdc.checkbox.MDCCheckbox(
            $(this).parent('.mdc-checkbox').get(0));
        $.each(_container.find('tr td:first-of-type div.mdc-checkbox'),
            function(index, item) {
              new mdc.checkbox.MDCCheckbox(item).checked = checkbox.checked;
              checkbox.checked ?
                  $(item).parents('tr').addClass('is-selected') :
                  $(item).parents('tr').removeClass('is-selected');
            });
        // 自动触发点击事件
        let selectItems = getSelectItems();
        noneSelection();
        singleSelection(selectItems);
        multipleSelection(selectItems);
      });
    }

    /**
     * 选中事件
     * @param obj 点选元素
     */
    function bindCheckEvt(obj) {
      obj.change(function() {
        $(this).parents('tr').toggleClass('is-selected');
        // 自动触发单选/多选事件
        let selectItems = getSelectItems();
        noneSelection();
        singleSelection(selectItems);
        multipleSelection(selectItems);
      });
    }

    /**未选事件*/
    function noneSelection(selectItems) {
      selectItems = selectItems || getSelectItems();
      if (selectItems.length === 0 && _singleSelect) {
        _noneSelect();
      }
    }

    /**单选事件*/
    function singleSelection(selectItems) {
      selectItems = selectItems || getSelectItems();
      if (selectItems.length === 1 && _singleSelect) {
        _singleSelect();
      }
    }

    /**多选事件*/
    function multipleSelection(selectItems) {
      selectItems = selectItems || getSelectItems();
      if (selectItems.length > 1 && _multipleSelect) {
        _multipleSelect();
      }
    }

    /**获取已选择项*/
    function getSelectItems() {
      let selectItems = [];
      $.each(_container.find('tbody tr td:first-of-type div.mdc-checkbox'),
          function(index, item) {
            if (new mdc.checkbox.MDCCheckbox(item).checked) {
              selectItems.push(dataList[index]);
            }
          });
      console.debug(selectItems);
      return selectItems;
    }

    /**
     * 对外暴露的方法
     * @constructor
     */
    function Handler() {
      // 拉取服务器数据
      this.pullData = pullData;
      // 单选事件
      this.singleSelect = _singleSelect;
      // 多选事件
      this.multipleSelect = _multipleSelect;
      // 按钮着色
      this.colorPageBtn = colorPageBtn;
    }

    return mdcDataTable;
  };

  $.mdcDataTable = function(opts) {
    return $(opts.container).mdcDataTable(opts);
  };

  /***
   * type: 1: alert,2: confirm,3: modal
   * @param opts
   */
  $.dialog = function(opts) {
    let paras = $.extend({
      title: '提示',
      content: '提示内容',
      width: null,
      // 标题位置,可选值: left, center, right
      titleAlign: 'left',
      // 弹窗类型: 1:提示框(一个按钮) 2:确认框(默认两个按钮) 3: 模态框(默认两个按钮) 4: 消息(无按钮)
      type: 1,
      // 是否显示标题
      showTitle: true,
      // 是否显示底部按钮
      showButton: true,
      contentType: 1,
      // 遮罩层关闭
      shadeClose: true,
      cancelBtn: {
        show: true,
        content: '取消',
      },
      confirmBtn: {
        show: true,
        content: '确定',
      },
      openedCallback: function() {
      },
      cancelCallback: function() {
      },
      confirmCallback: function() {
      },
    }, opts || {});
    let _title = paras.title;
    let _content = paras.content;
    let _showTitle = paras.showTitle;
    let _showButton = paras.showButton;
    let _width = paras.width;
    console.log($.strFormat('弹窗宽度: {}', _width));
    let _titleAlign = paras.titleAlign;
    let _contentType = paras.contentType;
    let _shadeClose = paras.shadeClose;
    let _type = paras.type;
    let _openedCallback = paras.openedCallback;
    let _cancelCallback = paras.cancelCallback;
    let _confirmCallback = paras.confirmCallback;
    let randomId = $.generateUuid(4);
    let myDialogTitleId = 'my-dialog-title-' + randomId;
    let myDialogContentId = 'my-dialog-content-' + randomId;
    let $container = $(`<div class="mdc-dialog"
      role="alertdialog"
      aria-modal="true"
      aria-labelledby="${myDialogTitleId}"
      aria-describedby="${myDialogContentId}">
      <div class="mdc-dialog__container">
      <div class="mdc-dialog__surface">
        <!-- Title cannot contain leading whitespace due to mdc-typography-baseline-top() -->
        ${_showTitle ? (`<h2 class="mdc-dialog__title" id="${myDialogTitleId}" 
            ${_titleAlign !== 'left'
        ? `style="text-align: ${_titleAlign}"`
        : ``}>${_title}</h2>`) : ''}
        <div class="mdc-dialog__content" id="${myDialogContentId}" tabindex="0"></div>
        ${_type === 4
        ? '<footer style="height: 4px;"></footer>'
        : `${_showButton
            ? `<footer class="mdc-dialog__actions">
            ${_type !== 1
                ? `<button type="button" class="mdc-button mdc-dialog__button button-cancel" 
                      data-mdc-dialog-action="no">
                    <span class="mdc-button__label">取消</span>
                   </button>`
                : ``}
              <button type="button" class="mdc-button mdc-dialog__button button-confirm" data-mdc-dialog-action="yes">
                <span class="mdc-button__label">确定</span>
              </button>
              </footer>`
            : ``}`}
        </div>
      </div>
      <div class="mdc-dialog__scrim"></div>
    </div>`);
    _width ? $container.find('.mdc-dialog__surface').
        css({'width': _width, 'max-width': _width}) : '';
    $('body').append($container);
    if (_contentType === 2) {
      $('#' + myDialogContentId).append(_content);
    } else {
      $('#' + myDialogContentId).html(_content);
    }
    let dialog = new mdc.dialog.MDCDialog($container.get(0));
    dialog.open();
    // 禁用遮罩层关闭
    if (!_shadeClose) {
      $container.find('.mdc-dialog__scrim').click(function(e) {
        e.stopPropagation();
        e.preventDefault();
        return false;
      });
    }
    // 打开时触发
    // MDCDialog:opening
    dialog.listen('MDCDialog:opening', function(data) {
      console.debug('opening...');
    });
    // 打开后触发
    dialog.listen('MDCDialog:opened', function(data) {
      console.debug('opened...');
      $container.attr('aria-hidden', 'true');
      _openedCallback && _openedCallback(data);
    });

    // 关闭时触发
    dialog.listen('MDCDialog:closing', function(data) {
      console.debug('closing...');
      $container.removeAttr('aria-hidden');
      data.detail.action === 'yes' ?
          _confirmCallback(data) : _cancelCallback(data);
      // if (result !== false) {
      // }
      $(this).remove();
    });
    // 关闭后触发
    // MDCDialog:closed
    dialog.listen('MDCDialog:closed', function(data) {
      console.debug('closed...');
    });
    // 取消触发元素默认事件
    $container.find('*[data-mdc-dialog-action]').click(function() {
      let data = {};
      data.target = $container.get(0);
      ($(this).attr('data-mdc-dialog-action') === 'yes' ?
          _confirmCallback(data, dialog, $container) :
          _cancelCallback(data, dialog, $container)) !== false && function() {
        // $container.find('.mdc-dialog__surface').
        //     css({'transition': 'all .3s', 'transform': 'scale(.1),opacity(0)'});
        $container.addClass('mdc-fade-out-simplify');
        $container.find('.mdc-dialog__surface').addClass('mdc-fade-out');
        setTimeout(function() {
          $container.removeClass('mdc-dialog--open');
          $container.remove();
        }, 150);
      }();
      return false;
    });
  };

  // 提示框,默认无按钮,接受showButton
  $.alert = function(msg, callback) {
    $.dialog({
      type: 1,
      title: msg.title || undefined,
      content: msg.content || msg,
      shadeClose: false,
      confirmCallback: callback,
    });
  };

  // 确认框,默认两个按钮,接受showButton
  $.confirm = function(msg, confirmCallback, cancelCallback) {
    $.dialog($.extend({}, {
      type: 2,
      content: msg,
      confirmCallback: confirmCallback,
      cancelCallback: cancelCallback,
    }));
  };

  // 询问输入框,一个按钮,不接受showButton
  $.prompt = function(opts) {
    throw Error('暂未实现');
    // opts = opts || {};
    // $.dialog($.extend(opts, {type: 3}));
  };

  // 模态框,默认两个按钮,接受showButton
  $.modal = function(msg, confirmCallback, cancelCallback) {
    let param = {
      type: 3,
      confirmCallback: confirmCallback,
      cancelCallback: cancelCallback,
    };
    if (typeof msg === 'string') {
      param.content = msg;
    } else {
      $.extend(param, msg);
    }
    $.dialog(param);
  };

  // 消息框,无按钮
  $.msg = function(msg, callback) {
    $.dialog({
      type: 4,
      showTitle: false,
      showButton: false,
      content: msg.content || msg,
      shadeClose: true,
      confirmCallback: callback,
    });
  };

  function ajaxReq(url, data, success, error) {
    $.ajax({
      url: url,
      type: 'POST',
      data: data,
      success: success ? success : function(data) {
      },
      error: error ? error : function(data) {
      },
    });
  }

  $.put = function(url, data, success, error) {
    ajaxReq(url, $.extend(data, {'_method': 'PUT'}), success, error);
  };

  $.delete = function(url, data, success, error) {
    ajaxReq(url, $.extend(data, {'_method': 'DELETE'}), success, error);
  };

  /***
   * 校验给定字符串非空.
   * @param str
   * @returns {boolean}
   */
  $.nonEmpty = function(str) {
    return typeof str !== 'undefined' && str !== null && str.trim().length > 0;
  };
  /***
   * 校验字符串为空.
   * @param str
   * @returns {boolean}
   */
  $.isEmpty = function(str) {
    return !$.nonEmpty(str);
  };
  /***
   * 校验checkbox是否选中.
   * @param selector input checkbox 选择器
   * @returns {boolean}
   */
  $.checked = function(selector) {
    return $(selector).prop('checked') ||
        $(selector).prop('checked') === 'checked';
  };
  /***
   * 校验字符串是否为指定长度.
   * @param str 待校验字符串
   * @param allowEmpty 是否允许空,true: 是
   * @param allowMinLength 允许最小长度
   * @param allowMaxLength 允许最大长度
   * @param inValidMsg 校验失败提示信息
   * @param pattern 如果该参数不为空,前面的限制参数无效
   */
  $.valid = function(
      str, allowEmpty, allowMinLength, allowMaxLength, inValidMsg, pattern) {
    if (!allowEmpty && (typeof str === 'undefined' || str.length < 1)) {
      $.alert(inValidMsg);
      return false;
    }
    if (typeof str === 'undefined' || str.length < 1) {
      str = '';
    }
    if (pattern && !new RegExp(pattern, 'g').test(str) && str.length > 0) {
      $.alert(inValidMsg);
      return false;
    }
    if (allowMinLength && str.length < allowMinLength) {
      $.alert(inValidMsg);
      return false;
    }
    if (allowMaxLength && str.length > allowMaxLength) {
      $.alert(inValidMsg);
      return false;
    }
    return true;
  };

  /**
   * 时间格式化.
   * @param date Date 或者 Milliseconds
   * @param pattern eg: 'yyyy/MM/dd hh:mm:ss'
   */
  $.dateFormat = function(date, pattern) {
    if (typeof date === 'number') {
      date = new Date(date);
    }
    let obj = {
      'M+': date.getMonth() + 1,
      'd+': date.getDate(),
      'h+': date.getHours(),
      'm+': date.getMinutes(),
      's+': date.getSeconds(),
      'q+': Math.floor((date.getMonth() + 3) / 3),
      'S': date.getMilliseconds(),
    };
    if (/(y+)/.test(pattern)) {
      pattern = pattern.replace(RegExp.$1,
          (date.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (let k in obj) {
      if (new RegExp('(' + k + ')').test(pattern)) {
        pattern = pattern.replace(RegExp.$1,
            RegExp.$1.length === 1 ? obj[k] : ('00' + obj[k]).substr(
                ('' + obj[k]).length));
      }
    }
    return pattern;
  };

  /**
   * 数组减法.
   * @param arr1 被减数组
   * @param arr2 减去数组
   */
  $.arraySub = function(arr1 = [], arr2 = []) {
    let result = arr1.concat();
    if (result.length === 0) {
      return [];
    }
    arr2.forEach(function(val) {
      result.forEach(function(v, i) {
        if (v === val) {
          result.splice(i, 1);
        }
      });
    });
    return result;
  };

  /** 校验字符串是否是一个正整数. */
  $.isPositiveNum = function(str) {
    return str && Number(str) > 0;
  };

  /** 校验字符串是否是一个非正整数. */
  $.isNegativeNum = function(str) {
    return !$.isPositiveNum(str);
  };

  /** 字符串格式化,分隔符{}. */
  $.strFormat = function() {
    if (typeof arguments == 'undefined') {
      return '';
    }
    let oldStr = arguments[0];
    for (let i = 1; i < arguments.length; i++) {
      oldStr = oldStr.replace(/{}/, arguments[i]);
    }
    return oldStr;
  };

  /** 获取UUID,默认长度36 */
  $.generateUuid = function(length = 36) {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,
        function(c) {
          let r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
          return v.toString(16);
        }).substr(0, length);
  };

  $.fn.mdcTree = function(opts) {
    let paras = $.extend({
      data: [],
      // 主键
      id: 'id',
      // 上级属性
      parentId: 'parentId',
      // 是否有子元素属性
      hasChild: 'hasChild',
      // 显示文本
      label: 'name',
      // 显示图标
      icon: 'icon',
      // 默认展开所有
      expandAll: true,
      // 显示复选框,
      checkbox: true,
      // 添加自定义属性到节点,{属性名: 属性},如; {'data-value': 'value'}
      externalProp: {},
      // 切换触发事件
      toggleEvt: function() {
      },
      // 切换触发事件后触发的事件,始终会执行
      togglePostEvt: function() {
      },
    }, opts || {});
    let _data = paras.data;
    let _parentId = paras.parentId;
    let _hasChild = paras.hasChild;
    let _id = paras.id;
    let _label = paras.label;
    let _expandAll = paras.expandAll;
    let _checkbox = paras.checkbox;
    let _externalProp = paras.externalProp;
    let _toggleEvt = paras.toggleEvt;
    let _togglePostEvt = paras.togglePostEvt;
    let containNode = $(this);
    console.debug(_data);
    // 顶层节点
    let roots = [];
    _data.forEach(function(val) {
      if (Number(val[_parentId]) <= 0) {
        roots.push(val);
      }
    });
    let children = $.arraySub(_data, roots);

    roots.forEach(function(val) {
      let divNode = $('<div class="mdc-tree-heading-container"></div>');
      const checkNodeStr =
          `<div class="mdc-checkbox"><input type="checkbox" class="mdc-checkbox__native-control" value="${val[_id]}"/><div class="mdc-checkbox__background"><svg class="mdc-checkbox__checkmark" viewBox="0 0 24 24"><path class="mdc-checkbox__checkmark-path" fill="none" d="M1.73,12.91 8.1,19.28 22.79,4.59"></path></svg><div class="mdc-checkbox__mixedmark"></div></div></div>`;
      let h3Node = $(
          `<h3 class="mdc-tree-heading mdc-tree-link" 
                data-ratio="1"
                data-id="${val[_id]}">${val[_label]}</h3>
          ${(_hasChild && val[_hasChild])
              ? `<i class="material-icons toggle-cell">keyboard_arrow_left</i>`
              : ''}`);
      for (let key in _externalProp) {
        $.trim(val[_externalProp[key + '']]) &&
        h3Node.attr(key + '', val[_externalProp[key + '']]);
      }

      let ulNode = $(`<ul class="mdc-tree-list${_expandAll
          ? ''
          : ' mdc-non-display'}"></ul>`);
      retrieveTree(val, ulNode, 2);
      _checkbox && divNode.append(checkNodeStr);
      divNode.append(h3Node);
      $(containNode).append(divNode).append(ulNode);
    });

    // 展开/收缩
    containNode.find('.mdc-tree-heading-container .mdc-tree-heading').
        on('click', function() {
          $(this).
              next('i.toggle-cell').
              toggleClass('mdc-tree-toggle__expanded');
          _toggleEvt && _toggleEvt(this);
          _togglePostEvt && _togglePostEvt(this);
          let headingContainer = $(this).
              parent('.mdc-tree-heading-container');
          // Tip: 如果没有复选框,只能有一个被选中
          if (!_checkbox) {
            console.debug('取消选中');
            containNode.find('.mdc-tree-heading-container.active').
                removeClass('active');
          }

          //  切换时始终保持选中状态
          // headingContainer.toggleClass('active').
          headingContainer.addClass('active').
              next('ul.mdc-tree-list').
              slideToggle(150);
          // 折叠,收起子元素
          if (headingContainer.hasClass('active')) {
            let childHeadingContainer = headingContainer.next(
                'ul.mdc-tree-list').find(
                '.mdc-tree-heading-container');
            console.log(childHeadingContainer);
            childHeadingContainer.next('ul.mdc-tree-list').slideUp(100);
            childHeadingContainer.removeClass('active');
            childHeadingContainer.children('i.toggle-cell').
                removeClass(
                    'mdc-tree-toggle__expanded');
          }
        });

    function retrieveTree(root, dom, depth = 1) {
      // 当前元素子元素
      let cs = children.filter(o => o[_parentId] === root[_id]);
      if (cs.length > 0) {
        cs.forEach(function(val) {
          let liNode = $(`<li class="mdc-tree-list__item"></li>`);
          dom.append(liNode);
          let divNode = $('<div class="mdc-tree-heading-container"></div>');
          const checkNodeStr =
              `<div class="mdc-checkbox"><input type="checkbox" class="mdc-checkbox__native-control" value="${val[_id]}"/><div class="mdc-checkbox__background"><svg class="mdc-checkbox__checkmark" viewBox="0 0 24 24"><path class="mdc-checkbox__checkmark-path" fill="none" d="M1.73,12.91 8.1,19.28 22.79,4.59"></path></svg><div class="mdc-checkbox__mixedmark"></div></div></div>`;
          let h3Node = $(
              `<h3 class="mdc-tree-heading mdc-tree-link mdc-ripple-upgraded"
                    data-ratio="${depth}"
                    data-id="${val[_id]}">${val[_label]}</h3>
               ${(_hasChild && val[_hasChild])
                  ?
                  `<i class="material-icons toggle-cell">keyboard_arrow_left</i>`
                  :
                  ''}`);
          for (let key in _externalProp) {
            $.trim(val[_externalProp[key + '']]) &&
            h3Node.attr(key + '', val[_externalProp[key + '']]);
          }

          let ulNode = $(`<ul class="mdc-tree-list${_expandAll
              ? ''
              : ' mdc-non-display'}"></ul>`);
          retrieveTree(val, ulNode, depth + 1);
          _checkbox && divNode.append(checkNodeStr);
          divNode.append(h3Node);
          liNode.append(divNode).append(ulNode);
        });
      }
    }

    class MDCTree {
      constructor(data, id, parentId, container) {
        this.data = data;
        this.id = id;
        this.parentId = parentId;
        this.container = container;
      }

      /** 当checkbox为false时,返回[] */
      getSelectItems = function() {
        let selectItems = [];
        $.each($(containNode).find(
            '.mdc-tree-heading-container .mdc-checkbox__native-control:checked'),
            (index, val) => {
              selectItems.push(val.value);
            });
        return selectItems;
      };

      // TODO-POST: 刷新提供策略,刷新当前节点,和刷新全部节点
      refresh = function(opts) {
        $(containNode).empty();
        $(containNode).mdcTree($.extend(paras, opts || {}));
      };

      /**
       * 选中指定id节点.
       * @param id 选中元素data-id
       * @param isTriggerEvt 是否触发事件,默认不触发
       */
      check(id, isTriggerEvt) {
        let _this = this;
        // 所有父节点
        let parentNodes = [];
        parentNodes.push(_this.container.find('h3[data-id="' + id + '"]'));
        let parents = _this.data.filter(
            item => Number(item[_this.id]) === Number(id));
        if (parents.length > 0) {
          getParents(parents[0][_this.parentId]);
        } else {
          let item = parentNodes[0];
          item.parent('div').addClass('active');
          isTriggerEvt && item.trigger('click');
          _togglePostEvt(item[0]);
        }

        function getParents(parentId) {
          // let parents = _this.data.filter(item => item[_this.id] === parentId);
          // let parent = parents && parents[0];
          let relativeParents = _this.data.filter(
              item => item[_this.id] === parentId);
          if (relativeParents.length > 0) {
            parentNodes.push(
                _this.container.find('h3[data-id="' + parentId + '"]'));
            getParents(relativeParents[0][_this.parentId]);
          }
        }

        // 级联展开
        while (parentNodes.length > 0) {
          let item = parentNodes.pop();
          parentNodes.length === 0 ? function() {
            item.parent('div').addClass('active');
            isTriggerEvt && item.trigger('click');
          }() : function() {
            item.trigger('click');
            item.parent().removeClass('active');
          }();
          _togglePostEvt(item[0]);
        }
      };

      /**
       * 判断给定的id元素是否为子节点. true:是.
       * @param id 选中元素data-id
       */
      isLeaf(id) {
        let _this = this;
        let isLeaf = !(_this.container.find('h3[data-id="' + id + '"]').
            parent('div').
            next('ul').text()) && true;
        console.log('isLeaf', isLeaf);
        return isLeaf;
      }
    }

    return new MDCTree(_data, _id, _parentId, containNode);
  };

}));
