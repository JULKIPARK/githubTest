var screenTablet = 1024;
var screenMobile = 960;
//function nav(){
//    $(".header .menu-wrap .menu > li > a.depth1").on('click',function(){
//        var header = $(this).parents(".header");
//        var li = $(this).closest("li");
//        if(li.hasClass("current")){
//            // header.removeClass("current");
//            // li.removeClass("current");
//            return false;
//        }else{
//            li.siblings().removeClass("current");
//            header.addClass("current");
//            li.addClass("current");
//        }
//
//    })
//    $(".header .head-top .btn-close-wrap").on('click',function(){
//        $(this).parents(".header").removeClass("current");
//        $(this).parents(".header").find(".menu-wrap .menu > li").removeClass("current");
//
//    })
//
//}
function layerClose() {
  $(".modal-wrap .btn-close").on("click", function () {
    $(this).parents(".modal-wrap").find(".dimmed").hide();
    $(this).parents(".modal-wrap").removeClass("show");
  });
}
function tab() {
  $(".tab-wrap .tab-list li button").on("click", function () {
    var num = $(this).closest("li").index();
    var group = $(this).parents(".tab-wrap");

    if ($(this).hasClass("current")) {
      group.closest("li").removeClass("current");
      group.closest("li").removeClass("m-current");
      return false;
    } else {
      group.find(".tab-list li button").removeClass("current");
      group.find(".tab-list li button").removeClass("m-current");
      $(this).addClass("current");
      group.find(".tab-cont .cont").hide();
      group.find(".tab-cont .cont").eq(num).show();
      //$('.slider').slick('setPosition');
      if ($(".img-slide-sub").length) {
        $(".img-slide-sub").slick("setPosition");
      }
    }
  });
}
function btnCtl() {
  $(".esg02-wrap .btn-topic-wrap").on("click", function () {
    if ($(this).parents(".section").hasClass("current")) {
      $(this).parents(".section").removeClass("current");
      $(this).find("button span").text("펼치기");
    } else {
      $(this).parents(".section").addClass("current");
      $(this).find("button span").text("닫기");
    }
  });
}

function prSlide() {
  if ($(".img-slide").length) {
    $(".img-slide").slick({
      infinite: true,
      slidesToShow: 1,
      arrows: false,
      dots: true,
      accessibility: true,
      autoplay: true,
      asNavFor: ".slider-for",
      responsive: [
        {
          breakpoint: screenTablet,
          settings: {
            slidesToShow: 2,
            slidesToScroll: 2,
          },
        },
        {
          breakpoint: screenMobile,
          settings: {
            slidesToShow: 1,
            slidesToScroll: 1,
          },
        },
      ],
    });
  }
}
function cotentSlide() {
  if ($(".img-slide-sub").length) {
    $(".img-slide-sub").slick({
      infinite: false,
      slidesToShow: 3,
      arrows: true,
      dots: false,
      accessibility: true,
      autoplay: true,
      responsive: [
        {
          breakpoint: screenTablet,
          settings: {
            slidesToShow: 2,
            slidesToScroll: 2,
          },
        },
        {
          breakpoint: screenMobile,
          settings: {
            slidesToShow: 1,
            slidesToScroll: 1,
          },
        },
      ],
    });
  }
}
function txtSlide() {
  if ($(".txt-slide-sub").length) {
    $(".txt-slide-sub").slick({
      infinite: false,
      slidesToShow: 6,
      arrows: true,
      dots: false,
      accessibility: true,
      autoplay: true,
      responsive: [
        {
          breakpoint: screenTablet,
          settings: {
            slidesToShow: 2,
            slidesToScroll: 2,
          },
        },
        {
          breakpoint: screenMobile,
          settings: {
            slidesToShow: 1,
            slidesToScroll: 1,
          },
        },
      ],
    });
  }
}

function sliderfor() {
  if ($(".slider-for").length) {
    $(".slider-for").slick({
      slidesToShow: 1,
      slidesToScroll: 1,
      arrows: false,
      fade: true,
      autoplay: true,
      asNavFor: ".img-slide",
    });
  }
}

function curationSlide() {
  if ($(".curation-slide").length) {
    $(".curation-slide").slick({
      dots: true,
      arrows: false,
      infinite: false,
      autoplay: true,
      slidesToShow: 4,
      slidesToScroll: 4,

      responsive: [
        {
          breakpoint: screenTablet,
          settings: {
            slidesToShow: 3,
            slidesToScroll: 3,
            infinite: true,
            dots: true,
          },
        },
        {
          breakpoint: screenMobile,
          settings: {
            slidesToShow: 1,
            slidesToScroll: 1,
          },
        },
      ],
    });
  }
}
function talkBtn() {
  $(".talk-wrap .btn-ctl").on("click", function () {
    if ($(this).parents(".talk-wrap").hasClass("shot")) {
      $(this).parents(".talk-wrap").removeClass("shot");
      $(this).find(".arrow").addClass("down");
    } else {
      $(this).parents(".talk-wrap").addClass("shot");
      $(this).find(".arrow").removeClass("down");
    }
  });
}

function currentWrap() {
  $(".current-wrap button").on("click", function () {
    $(".current-wrap input").removeClass("current");
    if ($(this).hasClass("current")) {
      return false;
    } else {
      $(this).parents("li").siblings().find("button", "input").removeClass("current");
      $(this).addClass("current");
    }
  });
}
function currentWrap2() {
  $(".current-wrap a").on("click", function () {
    if ($(this).hasClass("current")) {
      return false;
    } else {
      $(this).parents("li").siblings().find("a").removeClass("current");
      $(this).addClass("current");
    }
  });
}
function topicTab() {
  $(".topic-list button").on("click", function () {
    if ($(this).hasClass("current")) {
      return false;
    } else {
      $(".topic-list .li button").removeClass("current");
      $(this).addClass("current");
    }
  });
}
function filterCtl() {
  $(".filter-toggle .btn-ctl").on("click", function () {
    if ($(this).closest(".filter-toggle").hasClass("current")) {
      $(this).closest(".filter-toggle").removeClass("current");
      $(this).closest(".filter-toggle").find(".filter-area").hide(200);
    } else {
      $(this).closest(".filter-toggle").addClass("current");
      $(this).closest(".filter-toggle").find(".filter-area").show(200);
    }
  });
}
function selectdesignx() {
  $(".select-box-wrap button").on("click", function () {
    $(this).closest(".select-box-wrap").toggleClass("current");
  });
}
// function datePicker(){
//     $(".datepicker").on('click',function(){
//         $(".keyword-wrap li button").removeClass('current');
//        $(this).addClass("current");
//     })
//     if($(".datepicker").length){

//          if($(".datepicker").parents().hasClass("datepicker-radio-mix")){
//             $(".datepicker").datepicker({
//                 ignoreReadonly:true ,
//                 showOn:"button",
//                 buttonImage:"../img/common/ico_date.png",
//                 buttonImageOnly:true,
//                 dateFormat: 'yy.mm.dd',
//                 monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
//                 dayNamesMin: ['일','월','화','수','목','금','토'],
//                 changeMonth: true, //월변경가능
//                 changeYear: true, //년변경가능
//                 showMonthAfterYear: true //년 뒤에 월 표시
//             });

//          }else{
//             $(".datepicker").datepicker({
//                 ignoreReadonly:true ,
//                 dateFormat: 'yy.mm.dd',
//                 monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
//                 dayNamesMin: ['일','월','화','수','목','금','토'],
//                 changeMonth: true, //월변경가능
//                 changeYear: true, //년변경가능
//                 showMonthAfterYear: true //년 뒤에 월 표시
//             });
//          }
//    }
// }
function datePicker() {
  $(".datepicker").on("click", function () {
    $(".keyword-wrap li button").removeClass("current");
    $(this).addClass("current");
  });
  if ($(".datepicker").length) {
    if ($(".datepicker").parents().hasClass("datepicker-radio-mix")) {
      $(".datepicker").datepicker({
        ignoreReadonly: true,
        showOn: "button",
        buttonImage: "/assets/img/common/ico_date.png",
        buttonImageOnly: true,
        dateFormat: "yy-mm-dd",
        monthNamesShort: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
        dayNamesMin: ["일", "월", "화", "수", "목", "금", "토"],
        changeMonth: true, //월변경가능
        changeYear: true, //년변경가능
        showMonthAfterYear: true, //년 뒤에 월 표시
      });
    } else {
      $(".datepicker").datepicker({
        ignoreReadonly: true,
        dateFormat: "yy-mm-dd",
        monthNamesShort: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
        dayNamesMin: ["일", "월", "화", "수", "목", "금", "토"],
        changeMonth: true, //월변경가능
        changeYear: true, //년변경가능
        showMonthAfterYear: true, //년 뒤에 월 표시
      });
    }
  }
}
function selectBox() {
  $(".table-wrap .btn-ctl-wrap .btn-ctl").on("click", function () {
    $(this).closest("table").toggleClass("tbody-hide");
  });
}
function molayer() {
  $(".m-nav-wrap .btn-menu").on("click", function () {
    $(this).closest(".m-nav-wrap").addClass("current");
    $(".head-top .search-wrap").hide();
  });
}
function mosrchClose() {
  $(".m-nav-wrap .btn-menu-close").on("click", function () {
    $(this).closest(".m-nav-wrap").removeClass("current");
    $(".head-top .search-wrap").show();
  });
}
function moClose() {
  $(".mo-filter-wrap .btn-menu-close").on("click", function () {
    $(this).closest(".mo-filter-wrap").removeClass("show");
  });
}
function moNav() {
  $(".m-nav .depth1").on("click", function () {
    $(this).toggleClass("current");
  });
}

$(function () {
  layerClose();
  // tab();
  prSlide();
  sliderfor();
  nav();
  curationSlide();
  cotentSlide();
  txtSlide();
  btnCtl();
  talkBtn();
  currentWrap();
  topicTab();
  filterCtl();
  datePicker();
  currentWrap2();
  selectBox();
  selectdesignx();
  molayer();
  mosrchClose();
  moClose();
  moNav();
});

/*staccatovivace 09.19추가*/

$(document).ready(function () {
  $(".alrT").on("click", function () {
    alert("준비중입니다.");
  });
});

$(document).ready(function () {
  $(".keywordVisual").slick({
    autoplay: true,
    arrows: true,
    dots: false,
    infinite: true,
    cssEase: "linear",
    speed: 300,
    slidesToShow: 3,
    slidesToScroll: 1,
    responsive: [
      {
        breakpoint: 1180,
        settings: {
          slidesToShow: 2,
        },
      },
      {
        breakpoint: 767,
        settings: {
          slidesToShow: 1,
        },
      },
    ],
  });

  $(".listVisual").slick({
    autoplay: true,
    speed: 500,
    arrows: false,
    dots: false,
    infinite: true,
    cssEase: "linear",
    speed: 300,
    slidesToShow: 1,
    slidesToScroll: 1,
    vertical: true,
  });

  $(".curation-slide").slick({
    autoplay: true,
    speed: 500,
    arrows: false,
    dots: false,
    infinite: true,
    cssEase: "linear",
    speed: 300,
    slidesToShow: 4,
    slidesToScroll: 1,
  });

  $(".img-slide-sub").slick({
    autoplay: true,
    speed: 500,
    arrows: true,
    dots: false,
    infinite: true,
    cssEase: "linear",
    speed: 300,
    slidesToShow: 3,
    slidesToScroll: 1,
  });
});

$(document).ready(function () {
  $(".btn-search").click(function () {
    $(".searchSlide").css("display", "block");
  });

  $(".total-btn-close").click(function () {
    $(".searchSlide").css("display", "none");
  });
});

$(document).ready(function () {
  $(".p-listA dd").click(function () {
    $(this).children(".subs").slideToggle();
    $(this).children(".plusbtn").toggleClass("toggle_on");
  });
});

$(document).ready(function () {
  $(".menu").mouseover(function () {
    $(".dropmenu").slideDown();
  });
  $(".dropmenu").mouseleave(function () {
    $(".dropmenu").slideUp();
  });

  $(".menu01").mouseover(function () {
    $(".dropmenu1").css("background", "#f2f2f2");
    $(".menu01 a").css("border-bottom", "3px solid #D04A02");
    $(".on a").css("border-bottom", "0px solid #D04A02");
  });

  $(".dropmenu1").mouseleave(function () {
    $(".dropmenu1").css("background", "#fff");
    $(".menu01 a").css("border-bottom", "0px solid #D04A02");
  });

  $(".menu01").mouseleave(function () {
    $(".dropmenu1").css("background", "#fff");
    $(".menu01 a").css("border-bottom", "0px solid #D04A02");
  });

  $(".dropmenu1").mouseover(function () {
    $(".dropmenu1").css("background", "#f2f2f2");
    $(".menu01 a").css("border-bottom", "3px solid #D04A02");
    $(".on a").css("border-bottom", "0px solid #D04A02");
  });

  $(".menu02").mouseover(function () {
    $(".dropmenu2").css("background", "#f2f2f2");
    $(".menu02 a").css("border-bottom", "3px solid #D04A02");
    $(".on a").css("border-bottom", "0px solid #D04A02");
  });

  $(".dropmenu2").mouseleave(function () {
    $(".dropmenu2").css("background", "#fff");
    $(".menu02 a").css("border-bottom", "0px solid #D04A02");
  });

  $(".menu02").mouseleave(function () {
    $(".dropmenu2").css("background", "#fff");
    $(".menu02 a").css("border-bottom", "0px solid #D04A02");
  });

  $(".dropmenu2").mouseover(function () {
    $(".dropmenu2").css("background", "#f2f2f2");
    $(".menu02 a").css("border-bottom", "3px solid #D04A02");
    $(".on a").css("border-bottom", "0px solid #D04A02");
  });

  $(".menu03").mouseover(function () {
    $(".dropmenu3").css("background", "#f2f2f2");
    $(".menu03 a").css("border-bottom", "3px solid #D04A02");
    $(".on a").css("border-bottom", "0px solid #D04A02");
  });

  $(".dropmenu3").mouseleave(function () {
    $(".dropmenu3").css("background", "#fff");
    $(".menu03 a").css("border-bottom", "0px solid #D04A02");
  });

  $(".menu03").mouseleave(function () {
    $(".dropmenu3").css("background", "#fff");
    $(".menu03 a").css("border-bottom", "0px solid #D04A02");
  });

  $(".dropmenu3").mouseover(function () {
    $(".dropmenu3").css("background", "#f2f2f2");
    $(".menu03 a").css("border-bottom", "3px solid #D04A02");
    $(".on a").css("border-bottom", "0px solid #D04A02");
  });

  $(".menu04").mouseover(function () {
    $(".dropmenu4").css("background", "#f2f2f2");
    $(".menu04 a").css("border-bottom", "3px solid #D04A02");
    $(".on a").css("border-bottom", "0px solid #D04A02");
  });

  $(".dropmenu4").mouseleave(function () {
    $(".dropmenu4").css("background", "#fff");
    $(".menu04 a").css("border-bottom", "0px solid #D04A02");
  });

  $(".menu04").mouseleave(function () {
    $(".dropmenu4").css("background", "#fff");
    $(".menu04 a").css("border-bottom", "0px solid #D04A02");
  });

  $(".dropmenu4").mouseover(function () {
    $(".dropmenu4").css("background", "#f2f2f2");
    $(".menu04 a").css("border-bottom", "3px solid #D04A02");
    $(".on a").css("border-bottom", "0px solid #D04A02");
  });
});
