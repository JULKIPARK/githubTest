var screenTablet = 1024;
var screenMobile = 960;

function nav() {
	// $(".header .menu-wrap .menu > li > a.depth1").on('click',function(){
	//     var header = $(this).parents(".header");
	//     var li = $(this).closest("li");
	//     if(li.hasClass("current")){
	//         // header.removeClass("current");
	//         // li.removeClass("current");
	//         return false;
	//     }else{
	//         li.siblings().removeClass("current");
	//         header.addClass("current");
	//         li.addClass("current");
	//     }
	//
	// })
	// $(".header .head-top .btn-close-wrap").on('click',function(){
	//     $(this).parents(".header").removeClass("current");
	//     $(this).parents(".header").find(".menu-wrap .menu > li").removeClass("current");
	//
	// })
}

function layerClose() {
	$(".modal-wrap .btn-close").on("click", function () {
		//$(this).parents(".modal-wrap").find(".dimmed").hide();
		$(this).parents(".modal-wrap").removeClass("show");
		$("html").css("overflow-y", 'scroll');
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
			$('.slider').slick('setPosition');
			if ($(".img-slide-sub").length) {
				$(".img-slide-sub").slick("setPosition");
			}
		}
		selectBox();
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
	$(".current-wrap button").not('.btn-period').on("click", function () {
		let winW = $(window).width();
		$(".current-wrap .btn-period").removeClass("current");
		if ($(this).hasClass("current")) {
			return false;
		} else {
			$(this).parents("li").siblings().find("button",
				"input").removeClass("current");
			$(this).addClass("current");
		}

		if ($(window).width() < 1180) {
			const btnLeft = $(this).offset().left;
			const btnW = $(this).outerWidth(true);
			$('.current-wrap').not(':animated').animate({
				scrollLeft: $('.current-wrap').scrollLeft() + btnLeft + -$(
					window).width() / 2 + btnW / 2
			}, 200);
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
				monthNamesShort: ["1월", "2월", "3월", "4월", "5월", "6월", "7월",
					"8월", "9월", "10월", "11월", "12월"],
				dayNamesMin: ["일", "월", "화", "수", "목", "금", "토"],
				changeMonth: true, //월변경가능
				changeYear: true, //년변경가능
				showMonthAfterYear: true, //년 뒤에 월 표시
			});
		} else {
			$(".datepicker").datepicker({
				ignoreReadonly: true,
				dateFormat: "yy-mm-dd",
				monthNamesShort: ["1월", "2월", "3월", "4월", "5월", "6월", "7월",
					"8월", "9월", "10월", "11월", "12월"],
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
	//cotentSlide();
	txtSlide();
	btnCtl();
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

/*여기부터 RYU작업*/

/*staccatovivace 09.19추가*/

//$(document).ready(function(){
//	$('.alrT').on('click', function(){
//	    alert('준비중입니다.');
//	});
//
//})
//

$(document).ready(function () {
	$(".alrT").click(function () {
		$(".alertPopup").css("display", "block");
	});

	$(".pop-close-btn").click(function () {
		$(".popup").css("display", "none");
	});
});

$(document).ready(function () {
	$(".btn_pause").click(function () {
		$(".keywordVisual").slick("slickPause");
	});

	$(".btn_play").click(function () {
		$(".keywordVisual").slick("slickPlay");
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

	$(".caseVisual").slick({
		autoplay: true,
		speed: 500,
		arrows: false,
		dots: true,
		infinite: true,
		cssEase: "linear",
		speed: 500,
		slidesToShow: 1,
		slidesToScroll: 1,
	});

	$(".topicVisual").slick({
		autoplay: true,
		speed: 500,
		arrows: true,
		dots: false,
		infinite: true,
		cssEase: "linear",
		speed: 300,
		slidesToShow: 4,
		slidesToScroll: 1,
		responsive: [
			{
				breakpoint: 1180,
				settings: {
					slidesToShow: 2,
					dots: false,
					arrows: false,
					customPaging: function (slider, i) {
						//FYI just have a look at the object to find available information
						//press f12 to access the console in most browsers
						//you could also debug or look in the source
						console.log(slider);
						return i + 1 + "/" + slider.slideCount;
					},
				},
			},
			{
				breakpoint: 767,
				settings: {
					slidesToShow: 1,
					dots: false,
					arrows: false,
					customPaging: function (slider, i) {
						//FYI just have a look at the object to find available information
						//press f12 to access the console in most browsers
						//you could also debug or look in the source
						console.log(slider);
						return i + 1 + "/" + slider.slideCount;
					},
				},
			},
		],
	});

	$(".btn_pause").click(function () {
		$(".topicVisual").slick("slickPause");
	});

	$(".btn_play").click(function () {
		$(".topicVisual").slick("slickPlay");
	});

});

$(document).ready(function () {
	$(".p-listA dd").click(function () {
		$(this).children(".subs").slideToggle();
		$(this).children(".plusbtn").toggleClass("toggle_on");
	});
});

$(document).ready(function () {
	$(".header_sj").on("mouseleave", function () {
		$(".dropmenu").not(":animated").stop().slideUp();
	});

	$(".menu li").mouseover(function () {
		$(".dropmenu").not(":animated").stop().slideDown();
	});

	$(".menu01").mouseover(function () {
		$(".dropmenu1").css("background", "#f2f2f2");
		$(".menu01 p").css("transform", "scaleX(1)");
		$(".menu01 p").css("background", "#D04A02");
	});

	$(".dropmenu1").mouseleave(function () {
		$(".dropmenu1").css("background", "#fff");
		$(".menu01 p").css("transform", "scaleX(0)");
	});

	$(".menu01").mouseleave(function () {
		$(".dropmenu1").css("background", "#fff");
		$(".menu01 p").css("transform", "scaleX(0)");
	});

	$(".dropmenu1").mouseover(function () {
		$(".dropmenu1").css("background", "#f2f2f2");
		$(".menu01 p").css("transform", "scaleX(1)");
		$(".menu01 p").css("background", "#D04A02");
	});

	$(".menu02").mouseover(function () {
		$(".dropmenu2").css("background", "#f2f2f2");
		$(".menu02 p").css("transform", "scaleX(1)");
	});

	$(".dropmenu2").mouseleave(function () {
		$(".dropmenu2").css("background", "#fff");
		$(".menu02 p").css("transform", "scaleX(0)");
	});

	$(".menu02").mouseleave(function () {
		$(".dropmenu2").css("background", "#fff");
		$(".menu02 p").css("transform", "scaleX(0)");
	});

	$(".dropmenu2").mouseover(function () {
		$(".dropmenu2").css("background", "#f2f2f2");
		$(".menu02 p").css("transform", "scaleX(1)");
	});

	$(".menu03").mouseover(function () {
		$(".dropmenu3").css("background", "#f2f2f2");
		$(".menu03 p").css("transform", "scaleX(1)");
	});

	$(".dropmenu3").mouseleave(function () {
		$(".dropmenu3").css("background", "#fff");
		$(".menu03 p").css("transform", "scaleX(0)");
	});

	$(".menu03").mouseleave(function () {
		$(".dropmenu3").css("background", "#fff");
		$(".menu03 p").css("transform", "scaleX(0)");
	});

	$(".dropmenu3").mouseover(function () {
		$(".dropmenu3").css("background", "#f2f2f2");
		$(".menu03 p").css("transform", "scaleX(1)");
	});

	$(".menu04").mouseover(function () {
		$(".dropmenu4").css("background", "#f2f2f2");
		$(".menu04 p").css("transform", "scaleX(1)");
	});

	$(".dropmenu4").mouseleave(function () {
		$(".dropmenu4").css("background", "#fff");
		$(".menu04 p").css("transform", "scaleX(0)");
	});

	$(".menu04").mouseleave(function () {
		$(".dropmenu4").css("background", "#fff");
		$(".menu04 p").css("transform", "scaleX(0)");
	});

	$(".dropmenu4").mouseover(function () {
		$(".dropmenu4").css("background", "#f2f2f2");
		$(".menu04 p").css("transform", "scaleX(1)");
	});
});

$(document).ready(function () {
	$(".onlineBtn").click(function () {
		if (_session_id == null) {
			alert("로그인 후 이용할 수 있습니다.");
			return;
		}
		$(".questionPop #regAskTtl").val("");//제목
		$(".questionPop #regAskTpCdSelect option:eq(0)").prop("selected", true);//유형 선택
		$(".questionPop #regAskCn").val("");//내용
		$(".questionPop #qagress").prop("checked", false);

		$(".questionPop").css("display", "block");
		$("html").css("overflow-y", "hidden");

	});
	$(".samil-qna").click(function () {
		if (_session_id == null) {
			alert("로그인 후 이용할 수 있습니다.");
			return;
		}
		$(".questionPop #regAskTtl").val("");//제목
		$(".questionPop #regAskTpCdSelect option:eq(0)").prop("selected", true);//유형 선택
		$(".questionPop #regAskCn").val("");//내용
		$(".questionPop #qagress").prop("checked", false);

		$(".questionPop").css("display", "block");

	});

	$(".pop-close-btn").click(function () {
		$(".popup").css("display", "none");
		$("html").css("overflow-y", "scroll");
	});

	$(".pop-close-btn").click(function () {
		//$(".modal-wrap").removeClass("show");
		$(this).parents(".modal-wrap").removeClass("show");
		$("html").css("overflow-y", "scroll");

	});

	$(".agreeView-btn").click(function () {
		$("#agressPop").addClass("show");
		$("html").css("overflow-y", "hidden");
	});

	$(".essentialView-btn").click(function () {
		$("#popEssentialView").addClass("show");
		$("html").css("overflow-y", "hidden");
	});

	$(".optionsView-btn").click(function () {
		$("#popOptionsView").addClass("show");
		$("html").css("overflow-y", "hidden");
	});

	$('#btnQReg').bind('click', function () {
		if ($(".questionPop #regAskTtl").val() == "") {
			alert("문의 제목을 입력해 주세요.");
			$(".questionPop #regAskTtl").focus();
			return;
		}
		if ($(".questionPop #regAskTpCdSelect option:selected").val()
			== undefined) {
			alert("문의 유형을 선택해 주세요.");
			$(".questionPop #regAskTpCdSelect").focus();
			return;
		}
		if ($(".questionPop #regAskCn").val() == "") {
			alert("문의 내용을 입력해 주세요.");
			$(".questionPop #regAskCn").focus();
			return;
		}
		if (!$(".questionPop #qagress").is(":checked")) {
			alert("개인정보 수집 및 이용방침에 동의해주세요.");
			$(".questionPop #qagress").focus();
			return;
		}
		var formData, param = {};
		formData = _formLib.getFormData("qregFrm");
		param = {
			"url": "/qna/insertQna"
			, "type": "post"
			, "data": formData
			, "async": false
			, "callback": function (data) {
				if (data.msg == "success") {
					alert("문의 등록이 완료되었습니다.");
					$(".questionPop").hide();
				}
			}
		};
		_comLib.callAjax(param);
	}); //문의·요청 등록

});

$(document).ready(function () {
	$(".keyword-box").click(function () {
		$(".keyword-drop").slideToggle();
	});

	$(".selectA").click(function () {
		$(".selectToggle").slideToggle();
	});

	$(".selectToggle li").click(function () {
		var txt = $(this).text();
		var data = $(this).data("val");
		$(".selectA data").text(txt).data("val", data);
	});

	$(".historyBtn").click(function () {
		$(".search-history").slideToggle();
	});

	$(".mo_dropmenu .nav").click(function () {
		$(".dropmenu-list").slideToggle();
		$(".mo_dropmenu .nav span").toggleClass("toggle");
	});
});

/* Hook */
window.onload = () => {
	let winW = $(window).width();
	if (winW < 1181) {
		$(".header_sj").off("mouseleave");
	}
	if (winW < 768) {
		$(".content-esg-education .img-slide-sub").slick("unslick");
	}
	gnbToggleHandle();
	gnbMenuToggleHandle();
	filterToggleHandle();
	sortToggleHandle();
	imgExtensionEvent();
	bottomContentScrollEvent();
	toggleEvent();
	daterangepicker();
	tooltipToggleEvent();
};
$(window).on("resize", () => {
	let winW = $(window).width();
	if (winW < 1181) {
		$(".header_sj").off("mouseleave");
	} else {
		$(".header_sj").on("mouseleave", function () {
			$(".dropmenu").slideUp();
		});
	}
	if (winW < 768) {
		//교육-모바일 슬라이드 안나오는 문제로 주석처리 - 타 페이지에서 문제가 있으면 주석 해제 $(".content-esg-education .img-slide-sub").slick("unslick");
	} else {
		$(".img-slide-sub")
		.not(".slick-initialized")
		.slick({
			infinite: false,
			slidesToShow: 3,
			arrows: true,
			dots: false,
			accessibility: true,
			autoplay: true,
			responsive: [
				{
					breakpoint: 1180,
					settings: {
						slidesToShow: 2,
						slidesToScroll: 2,
					},
				},
				{
					breakpoint: 768,
					settings: {
						slidesToShow: 1,
						slidesToScroll: 1,
					},
				},
			],
		});
	}
});

const gnbToggleHandle = () => {
	const menu = $(".dropmenu");
	let windowScroll;
	$(".header_sj .btn-menu").on("click", () => {
		windowScroll = $(window).scrollTop();
		$("body").addClass("scroll-fixed").css("top", -windowScroll);
		menu.addClass("on");
	});
	$(".dropmenu .btn-close").on("click", () => {
		menu.removeClass("on");
		$("body").removeClass("scroll-fixed");
		$("html,body").scrollTop(windowScroll);
	});
};

const gnbMenuToggleHandle = () => {
	const handle = $(".dropmenuList .btn-m-toggle");
	handle.on("click", function () {
		if (!$(this).closest("dd").hasClass("on")) {
			$(".dropmenuList dd").removeClass("on");
			$(this).closest("dd").addClass("on");
		} else {
			$(".dropmenuList dd").removeClass("on");
		}
	});
};

const filterToggleHandle = () => {
	const openHandle = $(".result-head .btn-filter"),
		closeHandle = $(".filter-wrap .btn-close");
	let windowScroll;
	openHandle.on("click", () => {
		$(".filter-wrap").addClass("on");
		windowScroll = $(window).scrollTop();
		$("body").addClass("scroll-fixed").css("top", -windowScroll);
	});
	closeHandle.on("click", () => {
		$(".filter-wrap").removeClass("on");
		$("body").removeClass("scroll-fixed");
		$("html,body").scrollTop(windowScroll);
	});
};

const sortToggleHandle = () => {
	const sorting = $(".sorting");
	sorting.each(function () {
		const target = $(this).find(".btn-m-sort");
		const layer = target.closest(".sorting").find(".sorting-wrap");
		target.on("click", () => {
			if (!layer.hasClass("on")) {
				layer.addClass("on");
			} else {
				layer.removeClass("on");
			}
		});
	});
	$("html").on("click", function (e) {
		if (!$(e.target).is(".sorting, .sorting *")) {
			$(".sorting-wrap").removeClass("on");
		}
	});
};

const imgExtensionEvent = () => {
	const target = $(".js-img-extension");
	let windowScroll;
	target.on("click", (e) => {
		windowScroll = $(window).scrollTop();
		$("body").addClass("scroll-fixed").css("top", -windowScroll);
		$("body").append(
			'<div class="overlay-img-popup"><div class="inner"><img src="'
			+ e.currentTarget.children[0].currentSrc
			+ '" /></div><button type="button">닫기</button></div>');
		$(".overlay-img-popup button").on("click", () => {
			$(".overlay-img-popup").remove();
			$("body").removeClass("scroll-fixed");
			$("html,body").scrollTop(windowScroll);
		});
	});
};

const bottomContentScrollEvent = () => {
	if ($(".esg02-wrap").length > 0) {
		$(window).on("scroll", () => {
			let winW = $(window).width(),
				handleHeight = winW > 767 ? 140 : 0,
				winSt = $(this).scrollTop(),
				winH = $(this).height(),
				footerOt = $(".section-talk-wrap")?.offset().top;
			if (winSt + winH - handleHeight > footerOt) {
				$(".talk-wrap").addClass("static").removeClass("shot");
			} else {
				$(".talk-wrap").removeClass("static").addClass("shot");
			}
		});
		$(".talk-wrap .btn-ctl").on("click", function () {
			const headerH = $("header").outerHeight();
			$("html,body")
			.stop()
			.animate(
				{scrollTop: $(".section-talk-wrap")?.offset().top - headerH},
				1000);
		});
	}
};

const toggleEvent = () => {
	$('.js-toggle').on('click', function () {
		if (!$(this).parent().hasClass('active')) {
			$(this).parent().addClass('active');
		} else {
			$(this).parent().removeClass('active');
		}
	});
}

const daterangepicker = () => {
	$('.js-daterangepicker').daterangepicker({
		autoApply: true,
		locale: {
			format: "YYYY-MM-DD",
			daysOfWeek: ["일", "월", "화", "수", "목", "금", "토"],
			monthNames: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월",
				"10월", "11월", "12월"]
		}
	});
}

function tooltipToggleEvent() {
	$('.btn-tooltip').on('click', function () {
		if (!$(this).hasClass('on')) {
			$(this).addClass('on');
		} else {
			$(this).removeClass('on');
		}
	});

	$('html').on('click', function (e) {
		if (!$(e.target).is(
			'.btn-tooltip, .btn-tooltip *, .tooltip-cont, .tooltip-cont *')) {
			$('.btn-tooltip').removeClass('on');
		}
	});
}

$(document).ready(function () {
	$(".searchSlide .dimmed").click(function () {
		$(".total-btn-close").click();
	})
	$(".btn-search").click(function () {
		$(".searchSlide .dimmed").show();
		$(".searchSlide .rightA").css("right", "0");
		$(".header_sj .bottom .m-rt-menu .mo-search-btn .btn-search").css(
			"display", "none");
		$(".header_sj .bottom .m-rt-menu .mo-search-btn .btn-close").css(
			"display", "block");

	});

	$(".total-btn-close").click(function () {
		$(".searchSlide .dimmed").hide();
		$(".searchSlide .rightA").css("right", "-100%");
		$(".header_sj .bottom .m-rt-menu .mo-search-btn .btn-search").css(
			"display", "block");
		$(".header_sj .bottom .m-rt-menu .mo-search-btn .btn-close").css(
			"display", "none");
	});

	$(".select-box-wrap a").click(function () {
		var txt = $(this).text();
		$(".select-box-wrap a").removeClass("checked");
		$(this).addClass("checked");
		$(this).closest(".select-box-wrap").find("button").text(txt);
		$(this).closest(".select-box-wrap").removeClass("current");
	});

});

function msChg(no) {
	for (i = 1; i <= 6; i++) {
		$(".msTitle_" + i).css("border-bottom", "1px solid #222");
		$(".msView_" + i).css("display", "none");
	}

	$(".msTitle_" + no).css("border-bottom", "3px solid #D04A02");
	$(".msView_" + no).css("display", "block");
}

function _search(ty, str) {
	addAuto(str);

	location.href = "/search/main?category=TOTAL&kwd=" + str + "&fields=" + ty;

}

$(document).ready(function () {
	var $doc = $(document);
	autoYN();

	loadAuto();
	$doc.on("click", ".autoDel", function (e) {
		e.preventDefault();
		var val = $(this).data("val");
		delAuto(val);
	})

	$doc.on("click", ".msView_1 .btn-all-del ,.search-history .delall",
		function (e) {
			e.preventDefault();
			delallAuto();
		})

	$doc.on("click", ".btn-history-off,.search-history .onoff", function (e) {
		e.preventDefault();
		var auto = window.localStorage.getItem('auto');

		if (auto == "Y") {
			window.localStorage.setItem('auto', 'N');
		} else {
			window.localStorage.setItem('auto', 'Y');
		}

		$(".search-history").slideUp();
		autoYN();
	})

	$doc.on("keydown", ".search-bar .historyBtn", function (key) {

		if (key.keyCode == 13) {//키가 13이면 실행 (엔터는 13)
			if ($(this).val() != "") {
				_search($(".selectA data").data("val"), $(this).val())
			}
		}
	})
	$doc.on("keyup", ".mobile-search-bar input[type=text]", function (key) {

		if (key.keyCode == 13 || (key.keyCode == 16 && key.shiftKey == true)) {//키가 13이면 실행 (엔터는 13)
			if ($(this).val() != "") {
				_search("", $(this).val())
			}
		}
	})
	$doc.on("click", ".search-bar .submit-btn", function (e) {
		e.preventDefault();
		if ($(".search-bar .historyBtn").val() != "") {
			_search($(".selectA data").data("val"),
				$(".search-bar .historyBtn").val())
		}
	})

	$doc.on("click", "#msearchIco", function (e) {
		e.preventDefault();
		if ($(".mobile-search-bar input[type=text]").val() != "") {
			_search("", $(".mobile-search-bar input[type=text]").val())
		}
	})


	// $doc.on("click", "#msearchIco", function (e) {
	// 	e.preventDefault();
	// 	$("DIV.mobile-search-bar FORM").submit();
	// });

	$doc.on("keydown", ".leftSearch", function (key) {

		if (key.keyCode == 13) {//키가 13이면 실행 (엔터는 13)
			if ($(this).val() != "") {
				_search($(".select-box-wrap button").text(), $(this).val())
			}
		}
	})
	$doc.on("click", ".btn-total-search", function (e) {
		e.preventDefault();
		if ($(".leftSearch").val() != "") {
			_search($(".select-box-wrap button").text(), $(".leftSearch").val())
		}
	})

})

function addAuto(val) {
	var auto = window.localStorage.getItem('auto');
	if (auto != "Y") {
		return;
	}

	var autoList = window.localStorage.getItem('autoList');
	var idate = moment().format('MM.DD');
	var Autoval = {'keyWd': val, 'date': idate};

	var inAuto = [];
	if (autoList != null) {
		jList = JSON.parse(autoList);
		for (var i = 0; i < jList.length; i++) {
			if (jList[i].keyWd != val) {
				inAuto.push(jList[i]);
			}
		}
	}
	inAuto.unshift(Autoval);

	window.localStorage.setItem('autoList', JSON.stringify(inAuto));
	loadAuto();
}

function delallAuto() {
	window.localStorage.removeItem('autoList');
	loadAuto();
}

function delAuto(val) {
	var autoList = window.localStorage.getItem('autoList');
	var inAuto = [];

	if (autoList != null) {
		jList = JSON.parse(autoList);

		for (var i = 0; i < jList.length; i++) {
			if (jList[i].keyWd != val) {
				inAuto.push(jList[i]);
			}
		}
	}

	window.localStorage.setItem('autoList', JSON.stringify(inAuto));
	loadAuto();
}

function loadAuto() {
	var autoList = window.localStorage.getItem('autoList');
	$(".msView_1 .auot-list ul").html("");
	$(".history-list").html("");
	if (autoList != null) {

		jList = JSON.parse(autoList);
		for (var i = 0; i < jList.length; i++) {

		}

		$("#tmpl_auto").tmpl(jList).appendTo(".msView_1 .auot-list ul");
		$("#tmpl_auto2").tmpl(jList).appendTo(".history-list");
	}
}

function autoYN() {

	var auto = window.localStorage.getItem('auto');

	if (auto == null) {
		auto = "Y";
		window.localStorage.setItem('auto', 'Y');
	}
	if (auto == "Y") {
		$(".msView_1 .btn-all-del ,.msView_1 .auot-list,.history-list,.search-history .delall ").show();
		$(".btn-history-off .line").text("끄기");
		$(".search-history .onoff").text("끄기");
	} else {
		$(".msView_1 .btn-all-del ,.msView_1 .auot-list,.history-list ,.search-history .delall").hide();
		$(".btn-history-off .line").text("켜기");
		$(".search-history .onoff").text("켜기");
	}
}

$(document).ready(function () {
	var $doc = $(document);

	$doc.on("click", ".auot-list li", function (e) {
		e.preventDefault();
		var txt = $(this).find(".word").text();
		$(".leftSearch").val(txt);

		_search($(".select-box-wrap button").text(), $(".leftSearch").val());
	});

	$doc.on("click", ".keyword-drop .txt", function (e) {
		e.preventDefault();
		$(".historyBtn").val($(this).text());
		_search($(".selectA data").text(), $(".historyBtn").val());
	});
	$doc.on("click", ".history-list .h-keyword", function (e) {
		e.preventDefault();
		$(".historyBtn").val($(this).text());
		$(".search-history").slideToggle();
		_search($(".selectA data").text(), $(".historyBtn").val());
	})

	$doc.on("click", "._pub_pop", function (e) {
		e.preventDefault();
		var link = $(this).data("link");
		window.open(link);
		$("#_contentPop").removeClass("show");
		$("html").css("overflow-y", 'scroll');
	})

	$doc.on("click", "._mmovice_pop", function (e) {
		e.preventDefault();
		var link = $(this).data("link");
		var title = $(this).data("title");
		var lclsf = $(this).data("lclsf");
		console.log("========>" + lclsf)
		if (lclsf && lclsf == '10') {
			$("#popMovieView .notice").empty();
		} else {
			$("#popMovieView .notice")[0].innerHTML = "위 컨텐츠는 원저작자의 저작물로서 법적 보호를 받으며 삼일회계법인의 의견과는 무관함을 알려드립니다.<br />본 동영상의 게시 중단을 원하는 저작권자는 본 웹사이트를 통하여 온라인 문의를 남겨주시기 바랍니다";
		}
		$("#_contentPop").removeClass("show");
		$("html").css("overflow-y", 'scroll');
		$("#popMovieView").addClass("show");
		$("#popMovieView .tit").html(title);
		$("#popMovieView .video-cont").html('<iframe src="' + link
			+ '" width="100%" height="450px" frameborder="0" allowfullscreen=""></iframe>');

	})
	$doc.on("click", "._movice_pop", function (e) {
		e.preventDefault();
		var link = $(this).data("link");
		var title = $(this).data("title");
		var lclsf = $(this).data("lclsf");
		console.log("========>" + lclsf)
		if (lclsf && lclsf == '10') {
			$("#popMovieView .notice").empty();
		} else {
			$("#popMovieView .notice")[0].innerHTML = "위 컨텐츠는 원저작자의 저작물로서 법적 보호를 받으며 삼일회계법인의 의견과는 무관함을 알려드립니다.<br />본 동영상의 게시 중단을 원하는 저작권자는 본 웹사이트를 통하여 온라인 문의를 남겨주시기 바랍니다";
		}
		$("#_contentPop").removeClass("show");
		$("html").css("overflow-y", 'scroll');
		$("#popMovieView").addClass("show");
		$("#popMovieView .tit").html(title);
		$("#popMovieView .video-cont").html('<video  src="' + link
			+ '" controls autoplay width="100%" height="450px" frameborder="0" allowfullscreen=""></video >');

	})
	$doc.on("click", "#popMovieView .pop-close-btn ,#popMovieView .btn-close",
		function (e) {
			e.preventDefault();
			$("#popMovieView .video-cont").html("");
			$('#popMovieView').removeClass('show');
		})
	$doc.on("click", "#_contentPop .btn-close", function (e) {
		e.preventDefault();
		$("#_contentPop").removeClass("show");
		$("html").css("overflow-y", 'scroll');
	})
	$doc.on("click", ".newsletter_nlogin .btn-white", function (e) {
		e.preventDefault();
		$(".newsletter_nlogin").removeClass("show");
		$("html").css("overflow-y", 'scroll');
	})

	$doc.on("click", "#popMovieMView .pop-close-btn ,#popMovieMView .btn-close",
		function (e) {
			e.preventDefault();
			$('#popMovieMView').removeClass('show');
			$("html").css("overflow-y", 'scroll');
		})
	$doc.on("click", "#newsPop .btn-close", function (e) {
		e.preventDefault();
		$("#newsPop").removeClass("show");
		$("html").css("overflow-y", 'scroll');
	})

	$doc.on("click", ".content_pop", function (e) {
		e.preventDefault();

		var contKindCd = $(this).data("kind");
		var contUid = $(this).data("uid");
		var contRmthdCd = $(this).data("cd");
		var href = $(this).attr("href");
		var url = "/common/content";
		if (contKindCd == "30") {
			url = "/common/mucontent";
		}

		var formData, param = {};
		param = {
			"url": url
			, "type": "post"
			, "data": {"contUid": contUid}
			, "async": false
			, "callback": function (data) {
				if (data.msg == "success") {
					if (contKindCd == "40") {
						//window.open(data.info[0].linkUrl);

						$("#newsPop").html("");
						$("#tmpl_contentNewsPop").tmpl(data.info).appendTo(
							"#newsPop");
						$("#newsPop").addClass("show");
						$("html").css("overflow-y", 'hidden');
					} else if (contKindCd == "30") {
						data.contNm = data.info[0].contNm;
						console.log(data);

						/* vodComnt 데이터 변환 처리 */
						data.info.forEach(function(item, idx) {
							// var htmlObject;
							if (item.vodComnt.indexOf('[||]') > -1 || item.vodComnt.indexOf('[{1}]') > -1 || item.vodComnt.indexOf('[{2}]') > -1) {
								var htmlObject = $('<dl class="dep-1"></dl>');
								var tarArry = item.vodComnt.split("[||]");
								var lv1Pos = -1;
								var lv2Pos = -1;
								tarArry.forEach(function(item2, idx2) {
									// Lv.1
									if (item2.indexOf('[{1}]') === 0) {
										++lv1Pos;
										lv2Pos = -1;
										htmlObject.append(`<dd>${item2.replaceAll('[{1}]', '')}</dd>`);
									}
									// Lv.2
									if (item2.indexOf('[{2}]') === 0) {
										if (lv2Pos === -1) {
											htmlObject.find('DD:eq(' + lv1Pos + ')').append(`<ul></ul>`);
											++lv2Pos;
										}
										htmlObject.find('DD:eq(' + lv1Pos + ') > UL').append(`<li>${item2.replaceAll('[{2}]', '')}</li>`);
									}
								});
								data.info[idx].vodComnt = htmlObject.prop('outerHTML');
							}
						});

						$("#popMovieMView").html("");
						$("#tmpl_contentMPop").tmpl(data).appendTo("#popMovieMView");
						$("#popMovieMView").addClass("show");
					} else {
						$("#_contentPop").html("");
						$("#tmpl_contentPop").tmpl(data.info[0]).appendTo(
							"#_contentPop");
						$("#_contentPop").addClass("show");
						$("html").css("overflow-y", 'hidden');
					}
				} else {
					alert(data.msg);
				}
			}
		};
		_comLib.callAjax(param);

	})

})

function WinPopup(mypage, myname, w, h, scroll) { //v2.0
	var winl = (screen.width - w) / 2;
	var wint = (screen.height - h) / 2;

	try {
		winprops = 'height=' + h + ',width=' + w + ',top=' + wint + ',left='
			+ winl + ',resizable=0,status=0,scrollbars=' + scroll + '';
		var popWin = window.open(mypage, myname, winprops)
		if (parseInt(navigator.appVersion) >= 4) {
			popWin.window.focus();
		}
		if (!popWin) {
			alert('팝업이 차단되었습니다!');
		}
	} catch (e) {
		alert("팝업이 차단되었습니다!");
	}
}

$(document).ready(function () {
	var observer = new MutationObserver(function(mutations) {
		mutations.forEach(function(mutation) {
			if (mutation.attributeName === "class") {
				var attributeValue = $(mutation.target).prop(mutation.attributeName);
				if ($(mutation.target).hasClass('show')) {
					$("html").css("overflow-y",'hidden');
				} else {
					$("html").css("overflow-y",'scroll');
				}
			}
		});
	});

	var config = { attributes: true, childList: true, characterData: true };

	observer.observe(document.querySelector('DIV.modal-wrap'), config);
})


