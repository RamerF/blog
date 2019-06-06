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

  /**
   *  use ajax to get data(for org.springframework.data.domain.Page) and append table .
   * @param  opts
   *  {
   *      url, queryParams, container,
   *      columns:[
   *          data: the propId,
   *          editable: enable edit,
   *          render: the data render.
   *      ],
   *      thead: [
   *          data: the thead content,
   *          width: the thead width.
   *      ],
   *      callback
   *  }
   *
   */
  class AjaxTable {
    constructor(data = [], container) {
      this.container = container;
      this.data = data;
    }

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
  }

  $.fn.ajaxTable = function(opts) {
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
    let _type = paras.type;
    let _queryParams = paras.queryParams;
    let _container = this;
    let _columns = paras.columns;
    let _check = paras.check;
    let _thead = paras.thead;
    let _pageSize = paras.pageSize;
    let _pageList = paras.pageList;
    let _pageNumber = paras.pageNumber;
    let _successCallback = paras.success;
    let _errorCallback = paras.error;
    let ajaxData = [];
    if (_url == null) {
      console.error('请求地址不正确');
      return false;
    }
    let tableNode = $(
        '<table class="mdc-data-table mdc-shadow--2dp"></table>');
    let theadNode = $('<thead></thead>');
    let tbodyNode = $('<tbody></tbody>');
    let tfootNode = $('<tfoot></tfoot>');
    let trNode = $('<tr></tr>');
    const checkNodeStr =
        '<div class="mdc-form-field"><div class="mdc-checkbox"><input type="checkbox" class="mdc-checkbox__native-control"/><div class="mdc-checkbox__background"><svg class="mdc-checkbox__checkmark" viewBox="0 0 24 24"><path class="mdc-checkbox__checkmark-path" fill="none" d="M1.73,12.91 8.1,19.28 22.79,4.59"></path></svg><div class="mdc-checkbox__mixedmark"></div></div></div></div>';
    if (_check) {
      let checkNodeTh = $(
          '<th class="mdc-data-table__cell--non-numeric"></th>');
      checkNodeTh.append($(checkNodeStr));
      bindCheckAllEvt(checkNodeTh.find('input[type=checkbox]'));
      trNode.append(checkNodeTh);
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
      trNode.append(thNode);
    });
    theadNode.append(trNode);
    let globalPage;
    _queryParams['page'] = _pageNumber;
    _queryParams['size'] = _pageSize;
    $.ajax({
      url: _url,
      type: _type,
      data: _queryParams,
      contentType: _contentType,
      success: function(page) {
        if (page.result !== true) {
          console.error('error to pull data');
          return false;
        }
        globalPage = page.data;
        // 总页数
        let totalPages = globalPage.totalPages;
        // 当前页号,从1开始
        let number = Number(globalPage.number + 1);
        // 总记录数
        let totalElements = globalPage['totalElements'];
        // 每页大小
        let size = globalPage.size;
        let pageContainer = $('<div class="pagination-container"></div>');
        let staticInfo = `<p class="count" id="ajaxTableCount">
                            显示第 ${(globalPage.number * size + 1)}
                            到第 ${(totalElements % size !== 0 && number + 1 ===
                                  totalPages) ? totalElements : number * size} 条记录，
                            总共 ${totalElements} 条记录 每页显示${_queryParams.size}条记录</p>`;
        $(pageContainer).append(staticInfo);
        initNumberBtn(pageContainer);
        let data = globalPage.content;
        $.extend(ajaxData, data);
        appendBodyData(data);
        tableNode.append(theadNode);
        tableNode.append(tbodyNode);
        tableNode.append(tfootNode);
        $(_container).append(tableNode);
        $(_container).append(pageContainer);
        updateNumberBtn(number, totalPages);
        $('button[id="prevBtn"],button[id="nextBtn"]').
        on('click', function(e) {
          number = globalPage.number + 1;
          _queryParams.page = $(this).attr('id') === 'prevBtn'
              ? --number
              : ++number;
          updateNumberBtn(number, globalPage.totalPages);
          ajaxNewData(_url, _queryParams, size, tbodyNode);
        });
        $('button[id*="pageBtn"]').on('click', function(e) {
          let numberClick = Number($(this).text());
          if (!$(this).hasClass('mdc-button--raised') || isNaN(numberClick)) {
            return false;
          }
          updateNumberBtn(numberClick, globalPage.totalPages);
          _queryParams.page = number = numberClick;
          ajaxNewData(_url, _queryParams, size, tbodyNode);
        });
      },
      error: function() {
        console.error('error to pull data');
        _errorCallback();
      }
    })
    ;
    _successCallback();

    function initNumberBtn(pageContainer) {
      let ul = $('<ul></ul>');
      let prevBtn = $(
          '<li><button class="mdc-button mdc-button--raised prev-btn" id="prevBtn">上一页</button></li>');
      $(ul).append(prevBtn);
      if (globalPage.totalPages >= 7) {
        for (let i = 1; i < 8; i++) {
          let li = $('<li></li>');
          let btn = $(
              `<button class="mdc-button mdc-ripple-upgraded" id="pageBtn${i}">${i}</button>`);
          $(li).append(btn);
          $(ul).append(li);
        }
      } else {
        for (let i = 1; i < globalPage.totalPages + 1; i++) {
          let li = $('<li></li>');
          let btn = $(
              `<button class="mdc-button mdc-ripple-upgraded" id="pageBtn${i}">${i}</button>`);
          $(li).append(btn);
          $(ul).append(li);
        }
      }
      let nextBtn = $(
          '<li><button class="mdc-button mdc-button--raised mdc-ripple-upgraded next-btn" id="nextBtn">下一页</button></li>');
      $(ul).append(nextBtn);
      $(pageContainer).append(ul);
    }

    function updateNumberBtn(number, totalPages) {
      // number = number + 1;
      $('#pageBtn7').text(totalPages);
      let $prevBtn = $('#prevBtn');
      let $nextBtn = $('#nextBtn');
      $prevBtn.prop('disabled', false);
      $nextBtn.prop('disabled', false);
      if (number === 1) {
        $prevBtn.prop('disabled', 'disabled');
        colorPageBtn('#pageBtn1');
      } else if (number === totalPages) {
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

    function colorPageBtn(selector) {
      $(selector).
      parents('ul').
      find('button[class*="mdc-button"]').
      addClass('mdc-button--raised');
      $(selector).
      removeClass('mdc-button--raised');
    }

    function ajaxNewData(url, ajaxData, size, tbody) {
      ajaxData.size = size;
      $.ajax({
        url: url,
        type: _type,
        data: ajaxData,
        contentType: _contentType,
        success: function(page) {
          console.log(JSON.stringify(page));
          globalPage = page.data;
          let data = globalPage.content;
          $(tbody).empty();
          appendBodyData(data);
          let staticInfo = `显示第 ${(globalPage.number * globalPage.size + 1)}
                            到第 ${((globalPage.number + 1) * globalPage.size)} 条记录，
                            总共 ${globalPage['totalElements']} 条记录 每页显示${_queryParams.size}条记录`;
          if (globalPage['totalElements'] % globalPage.size !== 0 &&
              globalPage.number + 1 === globalPage.totalPages) {
            staticInfo = `显示第 ${(globalPage.number * globalPage.size + 1)}
                          到第 ${globalPage['totalElements']} 条记录，
                          总共 ${globalPage['totalElements']} 条记录 每页显示${_queryParams.size}条记录`;
          }
          $('#ajaxTableCount').text(staticInfo);
          _successCallback();
        },
        error: function() {
          console.log('获取数据失败...');
        }
      });
    }

    function appendBodyData(data) {
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
            if (typeof (_col.render) != 'undefined') {
              if (typeof (editable) != 'undefined' && editable === true) {
                td = $(`<td><input type="text" value="${_col.render(
                    _obj[_col.data])}"/></td>`);
              } else {
                td = $(`<td>${_col.render(_obj[_col.data])}</td>`);
              }
            } else {
              if (typeof (editable) != 'undefined' && editable === true) {
                td = $(
                    `<td><input type="text" value="${_obj[_col.data]}"/></td>`);
              } else {
                td = $(`<td>${_obj[_col.data]}</td>`);
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

    function bindCheckEvt(obj) {
      obj.change(function() {
        $(this).parents('tr').toggleClass('is-selected');
      });
    }

    return new AjaxTable(ajaxData, _container);
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
    let $container = $(`<div class="mdc-dialog"
      role="alertdialog"
      aria-modal="true"
      aria-labelledby="my-dialog-title"
      aria-describedby="my-dialog-content">
      <div class="mdc-dialog__container">
      <div class="mdc-dialog__surface">
        <!-- Title cannot contain leading whitespace due to mdc-typography-baseline-top() -->
        <h2 class="mdc-dialog__title" id="my-dialog-title">${_title}</h2>
        <div class="mdc-dialog__content" id="my-dialog-content" tabindex="0">
          ${_content}
        </div>
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
      // Assuming contentElement references a common parent element with the rest of the page's content
      $container.attr('aria-hidden', 'true');
      console.log('open', data.srcElement);
    });

    dialog.listen('MDCDialog:closing', function(data) {
      $container.removeAttr('aria-hidden');
      console.log(data, data.srcElement);
      $(this).remove();
      data.detail.action === 'yes' ?
          _confirmCallback(data) : _cancelCallback(data);
    });
  };

  $.alert = function(opts) {
    opts = opts || {};
    if (typeof opts === 'string') {
      let content = opts;
      opts = {};
      opts.content = content;
    }
    $.dialog($.extend(opts, {type: 1}));
  };

  $.confirm = function(opts) {
    opts = opts || {};
    $.dialog($.extend(opts, {type: 2}));
  };

  $.prompt = function(opts) {
    throw Error('暂未实现');
    // opts = opts || {};
    // $.dialog($.extend(opts, {type: 3}));
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
   * @param regex 如果该参数不为空,前面的限制参数无效
   */
  $.valid = function(
      str, allowEmpty, allowMinLength, allowMaxLength, inValidMsg, regex) {
    if (!allowEmpty && (typeof str === 'undefined' || str.trim().length < 1)) {
      $.alert(inValidMsg);
      return false;
    }
    if (typeof str === 'undefined' || str.trim().length < 1) {
      str = '';
    }
    if (regex && !new RegExp(regex).test(str)) {
      $.alert({
        content: inValidMsg
      });
      return false;
    }
    if (allowMinLength && str.length < allowMinLength) {
      $.alert({
        content: inValidMsg
      });
      return false;
    }
    if (allowMaxLength && str.length > allowMaxLength) {
      $.alert({
        content: inValidMsg
      });
      return false;
    }
    return true;
  };
}));
