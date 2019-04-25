(function() {
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
   *      url, ajaxData, container,
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
  $.ajaxTable = function(opts) {
    var paras = $.extend({
      url: null,
      ajaxData: {},
      container: '.mdl-data-table',
      columns: {},
      thead: {},
      editable: false,
      callback: function() {
      },
    }, opts || {});
    var _url = paras.url;
    var _ajaxData = paras.ajaxData;
    var _container = paras.container;
    var _columns = paras.columns;
    var _thead = paras.thead;
    var _callback = paras.callback;
    if (_url == null) {
      console.error('url cannot be null');
      return false;
    }
    var table = $('<table class=\'ajax-data-table\'></table>');
    var thead = $('<thead></thead>');
    var tbody = $('<tbody></tbody>');
    var tfoot = $('<tfoot></tfoot>');
    var thr = $('<tr></tr>');
    for (_th in _thead) {
      var th;
      if (typeof (_thead[_th].width) != 'undefined') {
        th = $('<th width=\'' + _thead[_th].width + '\'>' + _thead[_th].data +
               '</th>');
      } else {
        th = $('<th>' + _thead[_th].data + '</th>');
      }
      thr.append(th);
    }
    thead.append(thr);
    var globalPage;
    $.get(_url, _ajaxData, function(page) {
      if (page.result !== true){
        consolg.error("error to pull data");
        return false;
      }
      globalPage = page.data;
      // total pages
      var totalPages = globalPage.totalPages;
      // current page number
      var number = globalPage.number;
      // total elements
      var totalElements = globalPage.totalElements;
      // size of per page
      var size = globalPage.size;
      var pageContainer = $('<div class=\'page-container\'></div>');
      var staticInfo = '<p class=\'count\' id=\'ajaxTableCount\'>from ' +
                       (number * size + 1) + ' to  '
                       + ((number + 1) * size) + ' ,total: ' + totalElements +
                       '</p>';
      if (totalElements % size != 0 && number + 1 == totalPages) {
        staticInfo = '<p class=\'count\' id=\'ajaxTableCount\'>from: ' +
                     (number * size + 1) + ' to:  '
                     + totalElements + ' ,total: ' + totalElements + '</p>';
      }
      $(pageContainer).append(staticInfo);
      initNumberBtn(pageContainer);
      var data = globalPage.content;
      appendBodyData(data);
      table.append(thead);
      table.append(tbody);
      table.append(tfoot);
      $(_container).append(table);
      $(_container).append(pageContainer);
      updateNumberBtn(number, totalPages);
      $(_container).find('button').click(function(e) {
        $(this).inkReaction(e);
      });
      $('button[id=\'prevBtn\'],button[id=\'nextBtn\']').click(function(e) {
        number = globalPage.number;
        if ($(this).attr('id').indexOf('prev') > -1) {
          _ajaxData.page = number - 1;
          number--;
        } else {
          _ajaxData.page = number + 1;
          number++;
        }
        updateNumberBtn(number, globalPage.totalPages);
        ajaxNewData(_url, _ajaxData, size, tbody);
      });
      $('button[id*=\'pageBtn\']').click(function(e) {
        var number = $(this).text();
        if ($(this).hasClass('md-flat-btn') || isNaN(number)) {
          return false;
        }
        number -= 1;
        updateNumberBtn(number, globalPage.totalPages);
        _ajaxData.page = number;
        ajaxNewData(_url, _ajaxData, size, tbody);
      });
    });
    _callback();

    function initNumberBtn(pageContainer) {
      var ul = $('<ul></ul>');
      var prevBtn = $(
          '<li><button class=\'md-btn md-raised-btn prev-btn\' id=\'prevBtn\'>Prev</button></li>');
      $(ul).append(prevBtn);
      if (globalPage.totalPages >= 7) {
        for (var i = 1; i < 8; i++) {
          var li = $('<li></li>');
          var btn = $(
              '<button class=\'md-btn md-raised-btn\' id=\'pageBtn' + i +
              '\'>' +
              i + '</button>');
          $(li).append(btn);
          $(ul).append(li);
        }
      } else {
        for (var i = 1; i < globalPage.totalPages + 1; i++) {
          var li = $('<li></li>');
          var btn = $(
              '<button class=\'md-btn md-raised-btn\' id=\'pageBtn' + i +
              '\'>' +
              i + '</button>');
          $(li).append(btn);
          $(ul).append(li);
        }
      }
      var nextBtn = $(
          '<li><button class=\'md-btn md-raised-btn next-btn\' id=\'nextBtn\'>Next</button></li>');
      $(ul).append(nextBtn);
      $(pageContainer).append(ul);
    }

    function updateNumberBtn(number, totalPages) {
      number = number + 1;
      $('#pageBtn7').text(totalPages);
      $('#prevBtn').prop('disabled', false);
      $('#nextBtn').prop('disabled', false);
      if (number == 1) {
        $('#prevBtn').prop('disabled', 'disabled');
        colorPageBtn('#pageBtn1');
      } else if (number == totalPages) {
        $('#nextBtn').prop('disabled', 'disabled');
        colorPageBtn('#pageBtn7');
      }
      if (totalPages <= 7) {
        for (var i = 1; i <= totalPages; i++) {
          $('#pageBtn' + i).text(i);
          if (i == number) {
            colorPageBtn('#pageBtn' + number);
          }
        }
        return false;
      }
      if (number <= 4) {
        for (var i = 2; i < 7; i++) {
          if (i == 6) {
            $('#pageBtn' + i).text('...');
          } else {
            $('#pageBtn' + i).text(i);
          }
          if (i == number) {
            colorPageBtn('#pageBtn' + number);
          }
        }
      } else if (number > totalPages - 4) {
        var btnNum = 2;
        for (var i = totalPages - 5; i < totalPages; i++) {
          if (btnNum == 2) {
            $('#pageBtn' + btnNum).text('...');
          } else {
            $('#pageBtn' + btnNum).text(i);
          }
          if (i == number) {
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
      find('button[class*=\'md-flat-btn\']').
      removeClass('md-flat-btn').
      addClass(
          'md-raised-btn');
      $(selector).removeClass('md-raised-btn').addClass('md-flat-btn');
    }

    function ajaxNewData(url, ajaxData, size, tbody) {
      ajaxData.size = size;
      $.get(url, ajaxData, function(page) {
        globalPage = page;
        var data = globalPage.content;
        $(tbody).empty();
        appendBodyData(data);
        var staticInfo = 'from ' + (globalPage.number * globalPage.size + 1) +
                         ' to  '
                         + ((globalPage.number + 1) * globalPage.size) +
                         ' ,total: ' + globalPage.totalElements;
        if (globalPage.totalElements % globalPage.size != 0 &&
            globalPage.number + 1 == globalPage.totalPages) {
          staticInfo = 'from ' + (globalPage.number * globalPage.size + 1) +
                       ' to  '
                       + globalPage.totalElements + ' ,total: ' +
                       globalPage.totalElements;
        }
        $('#ajaxTableCount').text(staticInfo);
      });
    }

    function appendBodyData(data) {
      $.each(data, function(index) {
        var _obj = data[index];
        var tr = $('<tr></tr>');
        for (_prop in _columns) {
          var td;
          var editable = _columns[_prop].editable;
          if (typeof (_columns[_prop].render) != 'undefined') {
            if (typeof (editable) != 'undefined' && editable === true) {
              td = $('<td><input type=\'text\' value=\''
                     + _columns[_prop].render(_obj[_columns[_prop].data]) +
                     '\'/></td>');
            } else {
              td = $(
                  '<td>' + _columns[_prop].render(_obj[_columns[_prop].data]) +
                  '</td>');
            }
          } else {
            if (typeof (editable) != 'undefined' && editable == true) {
              td = $('<td><input type=\'text\' value=\'' +
                     _obj[_columns[_prop].data] + '\'/></td>');
            } else {
              td = $('<td>' + _obj[_columns[_prop].data] + '</td>');
            }
          }
          tr.append(td);
        }
        tbody.append(tr);
      });
    }
  };
})(jQuery);
