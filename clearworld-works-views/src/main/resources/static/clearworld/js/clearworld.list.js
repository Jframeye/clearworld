$(function () {
    // Waves初始化
    Waves.displayEffect();

    $('body').mCustomScrollbar({
        scrollInertia:150,
        scrollButtons:{
            enable:true
        }
    });

    // 设置input特效
    $(document).on('focus', 'input[type="text"]', function () {
        $(this).parent().find('label').addClass('active');
    }).on('blur', 'input[type="text"]', function () {
        if ($(this).val() == '') {
            $(this).parent().find('label').removeClass('active');
        }
    });
});