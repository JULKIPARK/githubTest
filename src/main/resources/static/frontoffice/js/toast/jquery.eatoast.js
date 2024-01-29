(function ($) {
    var toast = {},
    tmpl = '<div class="toast"><button class="close">&times;</button><span class="message">{{text}}</span></div>';

    // format the configuration
    function formatConf(config) {
        var _config = {
            duration: 3000,
            text: '',
            container: document.body,
            color: '#333',
            background: '#F5F5F5',
            border: 'none',
            style: '',
            autoclose: true,
            closeBtn: false,
            width: 'auto',
            animate: 'fade',
            align: 'top',
            speed: 'fast',
            opacity: 0.9,
            position: '50%'
        };

        if ($.isPlainObject(config)) {
            config = $.extend(_config, config);
        } else if ($.type(config) === 'string') {
            config = $.extend(_config, {
                text: config
            });
        } else {
            config = _config;
        }

        config.animate = config.animate.toLowerCase();
        config.width = typeof config.width === 'number' ? config.width + 'px' : config.width;
        config.align = config.align.toLowerCase() === 'top' ? 'top' : 'bottom';

        var totalHeight = config.container === document.body ? $(window).height : $(config.container).height();
        if ($.type(config.position) === 'string') {
            if (config.position.indexOf('%') === -1) {
                config.position = (parseInt(config.position) / totalHeight * 100) >> 0;
            }
            config.position = isNaN(config.position) ? '50%' : config.position.toString();
        } else if ($.isNumber(config.position)) {
            config.position = (config.position / totalHeight * 100) >> 0;
        }
        return config;
    }

    function toastAnimation($wrapper, config) {
        var closeAnimation = function () {};
        switch (config.animate) {
            case 'slide':
                var start = {
                    opacity: 0,
                    top: config.align === 'top' ? 0 : "100%"
                };
                var end = {
                    opacity: config.opacity,
                    top: config.align === 'top' ? config.position : (100 - parseInt(config.position) + "%")
                };

                $wrapper.css(start).show().css({
                    'margin-left': -$wrapper.width() / 2 + 'px'
                }).animate(end, config.speed);

                closeAnimation = function () {
                    $wrapper.animate(start, config.speed, function () {
                        $wrapper.remove();
                    });
                };
                break;

            case 'fade':
            default:
                $wrapper.css({
                    opacity: 0,
                    top: config.align === 'top' ? config.position : (100 - parseInt(config.position) + "%")
                })
                    .show()
                    .css({
                    'margin-left': -$wrapper.width() / 2 + 'px'
                })
                    .animate({
                    opacity: config.opacity
                });
                closeAnimation = function () {
                    $wrapper.fadeOut(config.speed,function(){
                        $wrapper.remove();
                    });
                };
                break;
        }

        // deal with close && autoclose
        $wrapper.find('.close').click(function () {
            closeAnimation();
        });
        config.autoclose && setTimeout(function () {
            closeAnimation();
        }, config.duration);
    }

    toast.show = function (config) {
        config = formatConf(config);

        var html = tmpl.replace('{{text}}', config.text.toString()),
            $toast = $(html).appendTo($(config.container)).hide(),
            $wrapper = $toast.wrap('<div class="toast-container"></div>').parent().hide();

        if (!config.style) {
            $toast.css({
                color: config.color,
                background: config.background,
                border: config.border,
                width: config.width
            });
        } else {
            $toast.addClass(config.style);
        }

        !config.closeBtn && $toast.find('.close').hide();
        $toast.show();
        $wrapper.css(config.align, config.position);
        toastAnimation($wrapper, config);
        return $wrapper;
    };

    $.each(['default', 'success', 'info', 'warn', 'danger'], function (i, prop) {
        toast[prop] = function (config) {
            var opt;
            if ($.isPlainObject(config)) {
                opt = config;
                opt.style = prop;
            } else if ($.type(config) === 'string') {
                opt = {
                    style: prop,
                    text: config
                };
            }

            return toast.show(opt);
        };
    });

    $.extend({
        toast: toast
    });
})(jQuery);