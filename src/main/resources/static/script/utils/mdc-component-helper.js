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
  /** 下拉框宽度修复,保持与父容器同宽 */
  $.fixSelectWidth = function() {
    $('.select-width-fixed').
    width($('.mdc-select.select-width-fixed').parent().width());
  };

  /** 初始化 mdc-text-field 组件 */
  $.fn.initMDCTextField = function() {
    $.each($(this).find('.mdc-text-field'), function(index, value) {
      console.debug(' init mdc text field ', value);
      new mdc.textField.MDCTextField(value);
    });
  };

  /** 初始化select框组件 */
  $.fn.initMDCSelect = function() {
    $.each($(this).find('.mdc-select'), function(index, value) {
      console.debug(' init mdc select ', value);
      new mdc.select.MDCSelect(value);
    });
    $.fixSelectWidth();
  };

}));
