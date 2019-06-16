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
  $.collapse = function(opts) {
    $(opts).find('h3.mdc-list-group__subheader').click(function() {
      $(this).next().slideToggle(150);
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
      $.each($(this.container).
          find('tbody tr td:first-of-type div.mdc-checkbox'),
          function(index, item) {
            if (new mdc.checkbox.MDCCheckbox(item).checked) {
              selectItems.push(ts.data[index]);
            }
          });
      return selectItems;
    }

    /**
     * 刷新表格
     * @param queryParams 查询参数
     * @param delCount 删除数目,用于执行删除操作重新计算页数
     */
    refresh(queryParams, delCount = 0) {
      let totalElements = this.queryParams.totalElements - delCount;
      let page = this.queryParams.page;
      let size = this.queryParams.size;
      // 删除元素: 第一页删除页号不变,否则判断当前页号是否大于删除后的数据总页号,更新页号
      // 删除元素后,点击刷新,有问题,内部页码没有更新
      let fixTotalPage = totalElements % size === 0
          ? ((totalElements / size) >>> 0)
          : ((totalElements / size) >>> 0) + 1;
      this.queryParams.page = page > fixTotalPage ? fixTotalPage : page;
      this.handler.pullData(this.queryParams, true);
    }
  }

  let mdcDataTableCountId = 'mdcDataTableCount';
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
      type: 'GET',
      dataType: 'json',
      contentType: 'application/x-www-form-urlencoded',
      // container: '#mdc-data-table',
      columns: [],
      check: true,
      thead: [{}],
      pageSize: 2,
      // 设置页面可以显示的数据条数
      pageList: [5, 10, 15, 20],
      // 首页页码
      pageNumber: 1,
      editable: false,
      success: function() {
      },
      error: function() {
      }
    }, opts || {});
    let _url = paras.url;
    let _contentType = paras.contentType;
    /** 请求方式 */
    let _type = paras.type;
    /** 查询条件 */
    let _queryParams = paras.queryParams;
    /** table父容器 */
    let _container = this;
    let _columns = paras.columns;
    let _check = paras.check;
    let _thead = paras.thead;
    let _pageSize = paras.pageSize;
    // 可供下拉选择的每页大小,暂未实现
    let _pageList = paras.pageList;
    let _pageNumber = paras.pageNumber;
    let _successCallback = paras.success;
    let _errorCallback = paras.error;
    if (_url == null) {
      console.error('请求地址不正确');
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
    let pageCountCode = $(`<p class="count" id="${mdcDataTableCountId}"></p>`);
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
    $(_container).append(tableNode).append(pageContainer);
    /** 服务器数据(数组),不包含分页信息 */
    let dataList = [];
    /** 分页信息,包含page,size */
    let pageInfo = {'page': _pageNumber, 'size': _pageSize};
    $.extend(_queryParams, pageInfo);
    pullData(_queryParams, true);
    _successCallback();
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
        success: function(page) {
          if (page.result !== true) {
            console.error('拉取数据失败 !');
            return false;
          }
          // 总页数
          pageInfo.totalPages = page.data.totalPages;
          // 总记录数
          pageInfo.totalElements = page.data.totalElements;
          // 当前页号,从1开始
          pageInfo.page = Number(page.data.number + 1);
          // 每页记录数
          pageInfo.size = page.data.size;
          // 数据内容
          dataList = page.data.content;
          mdcDataTable.data = dataList;
          console.debug(dataList);
          appendData(pageInfo, refreshBtn);
        },
        error: function() {
          console.error('拉取数据失败 !');
          _errorCallback();
        }
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
      $('#' + mdcDataTableCountId).text(staticInfo);
      updateNumberBtn(_pageInfo.page, _pageInfo.totalPages);
      // 上一页,下一页点击事件
      $('button[id="prevBtn"],button[id="nextBtn"]').
      unbind().
      on('click', function() {
        pageInfo.page = $(this).attr('id') === 'prevBtn'
            ? --_pageInfo.page
            : ++_pageInfo.page;
        updateNumberBtn(_pageInfo.page, _pageInfo.totalPages);
        pullData($.extend(_queryParams, _pageInfo), true);
      });
      // 页号点击事件
      $('button[id*="pageBtn"]').unbind().on('click', function(e) {
        let numberClick = Number($(this).text());
        if (!$(this).hasClass('mdc-button--raised') || isNaN(numberClick)) {
          return false;
        }
        _queryParams.page = _pageInfo.page = numberClick;
        updateNumberBtn(_pageInfo.page, _pageInfo.totalPages);
        pullData($.extend(_queryParams, _pageInfo), true);
      });
    }

    /** 初始化页码按钮 */
    function initNumberBtn() {
      $(pageBtnContainer).empty();
      let prevBtn = $(
          '<li><button class="mdc-button mdc-button--raised prev-btn" id="prevBtn">上一页</button></li>');
      $(pageBtnContainer).append(prevBtn);
      if (pageInfo.totalPages >= 7) {
        for (let i = 1; i < 8; i++) {
          let li = $('<li></li>');
          let btn = $(
              `<button class="mdc-button mdc-ripple-upgraded" id="pageBtn${i}">${i}</button>`);
          $(li).append(btn);
          $(pageBtnContainer).append(li);
        }
      } else {
        for (let i = 1; i < pageInfo.totalPages + 1; i++) {
          let li = $('<li></li>');
          let btn = $(
              `<button class="mdc-button mdc-ripple-upgraded" id="pageBtn${i}">${i}</button>`);
          $(li).append(btn);
          $(pageBtnContainer).append(li);
        }
      }
      let nextBtn = $(
          '<li><button class="mdc-button mdc-button--raised mdc-ripple-upgraded next-btn" id="nextBtn">下一页</button></li>');
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
      new mdc.checkbox.MDCCheckbox(
          $(checkNodeTh.find('input[type=checkbox]')).
          parent('.mdc-checkbox').
          get(0)).checked = false;
      $('#pageBtn7').text(totalPages);
      let $prevBtn = $('#prevBtn');
      let $nextBtn = $('#nextBtn');
      $prevBtn.prop('disabled', false);
      $nextBtn.prop('disabled', false);
      if (number === 1) {
        $prevBtn.prop('disabled', 'disabled');
        colorPageBtn('#pageBtn1');
      }
      if (number >= totalPages) {
        $nextBtn.prop('disabled', 'disabled');
        colorPageBtn('#pageBtn7');
      }
      if (totalPages <= 7) {
        for (let i = 1; i <= totalPages; i++) {
          $('#pageBtn' + i).text(i);
          if (i === number) {
            colorPageBtn('#pageBtn' + i);
          }
        }
        return false;
      }
      if (number <= 4) {
        for (let i = 2; i < 7; i++) {
          if (i === 6) {
            $('#pageBtn' + i).text('...');
          } else {
            $('#pageBtn' + i).text(i);
          }
          if (i === number) {
            colorPageBtn('#pageBtn' + number);
          }
        }
      } else if (number > totalPages - 4) {
        let btnNum = 2;
        for (let i = totalPages - 5; i < totalPages; i++) {
          if (btnNum === 2) {
            $('#pageBtn' + btnNum).text('...');
          } else {
            $('#pageBtn' + btnNum).text(i);
          }
          if (i === number) {
            colorPageBtn('#pageBtn' + btnNum);
          }
          btnNum++;
        }
      } else {
        $('#pageBtn2').text('...');
        $('#pageBtn3').text(number - 1);
        $('#pageBtn4').text(number);
        $('#pageBtn5').text(number + 1);
        $('#pageBtn6').text('...');
        colorPageBtn('#pageBtn4');
      }
    }

    /**
     * 选中页码按钮.
     * @param selector 选择器
     */
    function colorPageBtn(selector) {
      $(selector).
      parents('ul').
      find('button[class*="mdc-button"]').
      addClass('mdc-button--raised');
      $(selector).removeClass('mdc-button--raised');
    }

    /**
     * 拼接body数据
     * @param data 数据
     */
    function appendBodyData(data) {
      tbodyNode.empty();
      if (data) {
        data.forEach(function(_obj) {
          let tr = $('<tr></tr>');
          if (_check) {
            let checkNodeTd = $(
                '<td class="mdc-data-table__cell--non-numeric"></td>');
            checkNodeTd.append($(checkNodeStr));
            tr.append(checkNodeTd);
            bindCheckEvt(checkNodeTd.find('input[type=checkbox]'));
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
              td = $(`<td>${_col.render(val)}</td>`);
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
        $.each($(_container).find('tr td:first-of-type div.mdc-checkbox'),
            function(index, item) {
              new mdc.checkbox.MDCCheckbox(item).checked = checkbox.checked;
              checkbox.checked ?
                  $(item).parents('tr').addClass('is-selected') :
                  $(item).parents('tr').removeClass('is-selected');
            });
      });
    }

    /**
     * 选中事件
     * @param obj 点选元素
     */
    function bindCheckEvt(obj) {
      obj.change(function() {
        $(this).parents('tr').toggleClass('is-selected');
      });
    }

    /**
     * 对外暴露的方法
     * @constructor
     */
    function Handler() {
      this.pullData = pullData;
    }

    return mdcDataTable;
  };

  $.dialog = function(opts) {
    let paras = $.extend({
      title: '提示',
      content: '提示内容',
      type: 1,
      cancelCallback: function() {
      },
      confirmCallback: function() {
      }
    }, opts || {});
    let _title = paras.title;
    let _content = paras.content;
    let _type = paras.type;
    let _cancelCallback = paras.cancelCallback;
    let _confirmCallback = paras.confirmCallback;
    console.log(_content);
    let $container = $(`<div class="mdc-dialog"
      role="alertdialog"
      aria-modal="true"
      aria-labelledby="my-dialog-title"
      aria-describedby="my-dialog-content">
      <div class="mdc-dialog__container">
      <div class="mdc-dialog__surface">
        <!-- Title cannot contain leading whitespace due to mdc-typography-baseline-top() -->
        <h2 class="mdc-dialog__title" id="my-dialog-title">${_title}</h2>
        <div class="mdc-dialog__content" id="my-dialog-content" tabindex="0">${_content}</div>
        <footer class="mdc-dialog__actions">
          ${_type !== 1 ? `<button type="button" class="mdc-button mdc-dialog__button" data-mdc-dialog-action="no">
            <span class="mdc-button__label">取消</span>
          </button>` : ``}
        <button type="button" class="mdc-button mdc-dialog__button" data-mdc-dialog-action="yes">
          <span class="mdc-button__label">确定</span>
        </button>
        </footer>
        </div>
      </div>
      <div class="mdc-dialog__scrim"></div>
    </div>`);
    $('body').append($container);
    let dialog = new mdc.dialog.MDCDialog($container.get(0));
    dialog.open();
    dialog.listen('MDCDialog:opened', function(data) {
      $container.attr('aria-hidden', 'true');
    });

    dialog.listen('MDCDialog:closing', function(data) {
      $container.removeAttr('aria-hidden');
      $(this).remove();
      data.detail.action === 'yes' ?
          _confirmCallback(data) : _cancelCallback(data);
    });
  };

  $.alert = function(msg, callback) {
    $.dialog($.extend({}, {
      type: 1,
      content: msg,
      confirmCallback: callback
    }));
  };

  $.confirm = function(msg, confirmCallback, cancelCallback) {
    $.dialog($.extend({}, {
      type: 2,
      content: msg,
      confirmCallback: confirmCallback,
      cancelCallback: cancelCallback
    }));
  };

  $.prompt = function(opts) {
    throw Error('暂未实现');
    // opts = opts || {};
    // $.dialog($.extend(opts, {type: 3}));
  };

  function ajaxReq(url, data, success, error) {
    $.ajax({
      url: url,
      type: 'POST',
      data: data,
      success: success ? success : function(data) {},
      error: error ? error : function(data) {}
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
      'S': date.getMilliseconds()
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
}));
