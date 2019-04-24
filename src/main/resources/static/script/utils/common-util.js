(function() {
  $.collapse = function(opts) {
    // var paras = $.extend({
    //   tabContainer: 'tab-container',
    //   tab: 'tab',
    //   tabTitle: 'tab-title',
    //   tabContent: 'tab-content',
    //   activeTab: 'active'
    // }, opts || {});
    console.log(opts);
    $(opts).find('h3.mdc-list-group__subheader').click(function() {
      $(this).next().slideToggle(150);
    });
  };
})(jQuery);
