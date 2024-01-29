
//---------------------------------------------------------------------------
// 공통 기능
//---------------------------------------------------------------------------
var _comLib = (function(){

    // 내부 변수(private) 영역

    return {

        /**
         * ajax 호출 함수
         * @param param - 전송 파라메터(json)
         */
        callAjax : function (param) {
            var data = param.data || {};

            // 페이징 정보가 있을 경우 페이징 정보 추가
            if(typeof(param.paging) != "undefined" && param.paging == "Y") {
                data["paging"] = "Y";
                data["pageNumber"] = 1;
                data["pageSize"] = 10;

                if(typeof(param.pageNumber) != "undefined" && param.pageNumber != "") {
                    data.pageNumber = param.pageNumber;
                }

                if(typeof(param.pageSize) != "undefined" && param.pageSize != "") {
                    data.pageSize = param.pageSize;
                }
            }

            $.ajax({
                "url" : param.url,
                "async" : typeof(param.async) != "undefined" ? param.async : true,
                "type" : param.type || "post",
                "data" : param.data || {},
                "dataType" : param.dataType,
                "processData" : typeof(param.processData) != "undefined" ? param.processData : true,
                "contentType" : param.contentType,
                "beforeSend" : function(xmlHttpRequest) {
                    xmlHttpRequest.setRequestHeader("AJAX","true");
                    if(param.progress == "ok"){
                        $("#popLayerBg02").show();
                        $(".pop_working").show();
                    }
                },
                "complete" : function(){
                    if(param.progress == "ok"){
                        $("#popLayerBg02").hide();
                        $(".pop_working").hide();
                    }
                },
                "success" : function(data){
                    if (typeof(param.callback) == "function") {
                        param.callback(data);
                    }
                },
                "error" : function(xhr, status, error){
                    if (xhr.status == "403") {

                        _comLib.alertMsg("세션이 만료되었습니다.", function() {
                            $(location).attr("href", '/intro/loginView');
                        });
                    } else {

                        // 오류 메시지 여부에 따른 표출 처리
                        if(typeof(xhr.responseJSON) != "undefined" && typeof(xhr.responseJSON.msg) != "undefined" && xhr.responseJSON.msg != "") {

                            // 서버에서 별도 전송한 메시지가 있을 경우 해당 메시지 표시
                            _comLib.alertMsg(xhr.responseJSON.msg);
                        } else {
                            _comLib.alertMsg("처리 중 오류가 발생했습니다.");
                        }
                    }
                }
            });
        },

        /**
         * 달력 생성
         * 시작일ex) _comLib.setDatePicker("schDateSt","schDateEd", "yy-mm-dd");
         * 종료일ex) _comLib.setDatePicker("schDateEd", null, "yy-mm-dd");
         * @param targetId - 달력을 생성할 대상 객체ID
         * @param relayId - 달력이 닫힐 때 연달아 변경할 대상 객체ID
         * @param format - 달력 표시 형식(yymmdd)
         * @param fnOnClose - 달력이 닫힐 때 호출할 함수
         */
        setDatePicker : function(targetId, relayId, format, fnOnClose) {
            var option = {};

            var option = {
                "showOtherMonths" : true,   // 빈 공간에 현재월의 앞뒤월의 날짜를 표시
                "showMonthAfterYear" :true, // 월- 년 순서가아닌 년도 - 월 순서
                "changeYear" : true,        // option값 년 선택 가능
                "changeMonth" : true,       // option값 월 선택 가능
                "monthNamesShort" : ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'], //달력의 월 부분 텍스트
                "monthNames" : ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'], //달력의 월 부분 Tooltip
                "dayNamesMin" : ['일','월','화','수','목','금','토'], //달력의 요일 텍스트
                "dayNames" : ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'], //달력의 요일 Tooltip
                "yearRange" : "1900:2100",
                "onSelect" : function(val) {
                    $("#"+targetId).val(val);
                    $(this).change();
                }
            };

            // 달력 날짜 형식 설정
            if(typeof(format) != "undefined") {
                option["dateFormat"] = format;
            } else {
                option["dateFormat"] = "yymmdd";
            }

            // 달력 닫기 이벤트 설정
            if(typeof(fnOnClose) != "undefined") {
                option["onClose"] = fnOnClose;
            } else {
                if(typeof(relayId) != "undefined") {

                    option["onClose"] = function(selectedDate) {
                        // 시작일(fromDate) datepicker가 닫힐때
                        // 종료일(toDate)의 선택할수있는 최소 날짜(minDate)를 선택한 시작일로 지정
                        $("#"+relayId).datepicker( "option", "minDate", selectedDate );
                    }
                }
            }

            $("#"+targetId).datepicker(option);
        },

        /**
         * 페이징 영역 생성
         * ex) _comLib.setPagination("pagingAreaId", fnOnSelect, 12, 3);
         * @param targetId - 페이징 영역을 생성할 대상 객체ID
         * @param fnOnSelect - 페이지를 선택할 때 호출할 함수
         * @param totalPage - 총 페이지 수
         * @param pageNumber - 페이지 번호
         */
        setPagination : function(targetId, fnOnSelect, totalPage, pageNumber) {
            $("#"+targetId).twbsPagination({
                "totalPages" : Number(totalPage)
                , "startPage" : Number(pageNumber)
                , "visiblePages" : 10
                , first : "<span class='blind' style='display:inline;'>처음</span>"
                , firstClass : "btn first"
                , prev : "<span class='blind' style='display:inline;'>이전</span>"
                , prevClass : "btn prev"
                , activeClass : "on"
                , next : "<span class='blind' style='display:inline;'>다음</span>"
                , nextClass : "btn next"
                , last : "<span class='blind' style='display:inline;'>마지막</span>"
                , lastClass : "btn last"
                , "initiateStartPageClick" : false
                , "onPageClick" : function (event, page) {
                    fnOnSelect(page);
                }
            });
        },

        /**
         * 안내 메시지 팝업
         * @param msg - 메시지
         * @param callback - 콜백 함수
         */
        infoMsg : function (msg, callback, width, height) {

            var flag = true;

            // 메시지가 표출되고 있는 중에 연달아 호출 될 경우 기다렸다가 호출
            if ( $("#_infoMsg").is(":visible") != true ) {

                // 팝업 메시지 설정
                $("#_infoMsgBody").html("<p style='text-align: left;'>"+msg+"</p>");

                // 팝업 표시
                $("#_infoMsg").css("display","flex").hide().fadeIn(500, function() {
                    // 콜백 함수가 있을 경우 호출
                    if (typeof(callback) == "function" && flag == true) {
                        callback();
                    }
                });

                if(_formLib.isEmpty(width) != true){
                    console.log("cc");
                    $("#_infoMsg .pop_layer").css("width", width);
                }

                if(_formLib.isEmpty(height) != true){
                    $("#_infoMsg .pop_layer").css("height", height);
                }
            } else {
                var currMsg = $("#_infoMsgBody").html();
                var nextMsg = "<p>"+msg+"</p>";

                // 메시지가 다를 경우 1초 뒤 표시
                if (currMsg != nextMsg) {
                    setTimeout(function(){
                        _comLib.infoMsg(msg, callback);
                    },1000);
                }
            }
        },

        /**
         * 공통 메시지 팝업
         * @param msg - 메시지
         * @param callback - 콜백 함수
         */
        alertMsg : function (msg, callback) {

            var flag = true;

            // 메시지가 표출되고 있는 중에 연달아 호출 될 경우 기다렸다가 호출
            if ( $("#_alertMsg").is(":visible") != true ) {

                // 팝업 메시지 설정
                $("#_alertMsgBody").html("<p>"+msg+"</p>");

                // 팝업 표시
                $("#_alertMsg").css("display","flex").hide().fadeIn(500, function() {
                    setTimeout(function() {
                        $("#_alertMsg").fadeOut(500, function(){

                            // 콜백 함수가 있을 경우 호출
                            if (typeof(callback) == "function" && flag == true) {
                                callback();
                            }
                        });
                    }, 1200);
                });
            } else {
                var currMsg = $("#_alertMsgBody").html();
                var nextMsg = "<p>"+msg+"</p>";

                // 메시지가 다를 경우 1초 뒤 표시
                if (currMsg != nextMsg) {
                    setTimeout(function(){
                        _comLib.alertMsg(msg, callback);
                    },1000);
                }
            }
        },

        /**
         * 공통 확인창 팝업
         * @param msg - 메시지
         * @param callback - 콜백 함수
         */
        confirmMsg : function (msg, callback) {

            // 메시지가 표출되고 있는 중에 연달아 호출 될 경우 기다렸다가 호출
            if ( $("#_comfirmMsg").is(":visible") != true ) {

                // 팝업 메시지 설정
                $("#_comfirmMsgBody > #msg").html(msg);

                // 팝업 표시
                $("#_comfirmMsg").css("display", "flex").hide().fadeIn(500, function() {
                    $('#_comfirmMsgBody .btn-confirm').click(function(e){//확인버튼
                        // 콜백 함수가 있을 경우 호출
                        if (typeof(callback) == "function") {
                            callback();
                            _comLib.closePopLayer('_comfirmMsg');
                        }
                        $('#_comfirmMsgBody .btn-confirm').off("click");
                    });

                    $('#_comfirmMsgBody .btn-cancel').click(function(e){//취소버튼
                        _comLib.closePopLayer('_comfirmMsg');
                        $('#_comfirmMsgBody .btn-confirm').off("click");
                    });
                });
            } else {
                var currMsg = $("#_comfirmMsgBody").html();
                var nextMsg = "<p>"+msg+"</p>";

                // 메시지가 다를 경우 1초 뒤 표시
                if (currMsg != nextMsg) {
                    _comLib.confirmMsg(msg, callback);
                }
            }

        },

        /**
         * 윈도우 팝업 생성
         * @param formName - 전송 대상 폼 명칭
         * @param url - 전송 대상 URL
         * @param opt - 윈도우 팝업 옵션(json)
         * @returns 팝업 윈도우
         */
        openPopWin : function(formName, url, opt) {
            var winOpt = {
                "name" : _formLib.getValue(opt.name) == "" ? "bizPop" : opt.name,
                "height" : _formLib.getValue(opt.height) == "" ? "450" : opt.height,
                "width" : _formLib.getValue(opt.width) == "" ? "600" : opt.width,
                "top" : _formLib.getValue(opt.top) == "" ? "100" : opt.top,
                "left" : _formLib.getValue(opt.left) == "" ? "100" : opt.left,
                "menubar" : _formLib.getValue(opt.menubar) == "" ? "no" : opt.menubar,
                "scrollbars" : _formLib.getValue(opt.scrollbars) == "" ? "no" : opt.scrollbars,
                "resizable" : _formLib.getValue(opt.resizeable) == "" ? "no" : opt.resizeable,
                "toolbar" :  _formLib.getValue(opt.toolbar) == "" ? "no" : opt.toolbar,
                "status" : _formLib.getValue(opt.status) == "" ? "no" : opt.status
            };

            var popWin = window.open(url, winOpt.name, "height="+winOpt.height+",width="+winOpt.width+",menubar="+winOpt.menubar+",scrollbars="+winOpt.scrollbars+",resizable="+winOpt.resizable+",toolbar="+winOpt.toolbar+",status="+winOpt.status+",left="+winOpt.left+",top="+winOpt.top);

            //$("#"+formName).attr("action", url);
            //$("#"+formName).attr("target", winOpt.name);
            //$("#"+formName).submit();

            popWin.focus();

            return popWin;
        },

        /**
         * 레이어 팝업 생성
         * @param url - 전송 대상 URL
         * @param opt - 레이어 팝업 옵션(json)
         */
        openPopLayer : function(formData, url, opt, callback) {
            var target = "."+opt.target;

            $(target).fadeIn(300, function(){
                // 콜백 함수가 있을 경우 저장
                if (typeof(callback) == "function") {
                    _comLib.setData("callback", callback);
                }

                $(target).load(url,formData, function(){
                    // 닫기 버튼 이벤트 설정
                    $(target+" .btn_close").bind("click", function() {
                        //$('#popLayerBg').hide();
                        $(target).hide();
                        $(target).empty();
                    });
                });
            });
        },

        /**
         * 레이어 팝업 닫기
         * @param target - 대상 객체
         * @param type - 대상 객체
         * @returns 조회 값(null 일 경우 공백"" 반환)
         */
        closePopLayer : function(target, type) {
            $(".btn-confirm").text('확인');
            $(".btn-cancel").text('취소');

            if(_formLib.isEmpty(target)){
                // 레이어 팝업 내용 삭제 및 숨김
                $("#popLayerBg, .pop_layer").hide();
            }else{
                // 레이어 팝업 내용 삭제 및 숨김
                $("#"+target).hide();
                _formLib.resetLayer(target, type);
            }
        },

        /**
         * 쿠키 조회
         * @param name - 조회 대상 쿠키 이름
         * @returns 쿠키 값
         */
        getCookie : function(name) {
            var cookieList = document.cookie.split(";");
            var cookie;

            for (var i = 0; i < cookieList.length; i++) {
                var key = cookieList[i].substr(0, cookieList[i].indexOf("=")).replace(/^\s+|\s+$/g, "");
                var val = cookieList[i].substr(cookieList[i].indexOf("=") + 1);

                if (key == name) {
                    cookie = unescape(val);
                }
            }

            return cookie;
        },

        /**
         * 쿠키 설정
         * @param name - 쿠키 이름
         * @param value - 쿠키에 저장할 값
         * @param expireDays - 만료일자
         */
        setCookie : function(name, value, expireDays) {
            var todayDate = new Date();

            // 기존 쿠키 삭제
            _comLib.delCookie(name);

            //  쿠키 설정
            todayDate.setDate(todayDate.getDate() + expireDays);
            document.cookie = name + "=" + escape(value) + "; expires=" + todayDate.toGMTString() + "; path=/;";
        },

        /**
         * 쿠키 값 삭제
         * @param name - 쿠키 이름
         */
        delCookie : function(name) {
            var todayDate = new Date();

            todayDate.setDate(todayDate.getDate()-1);

            document.cookie = name + "=; expires="+todayDate.toGMTString()+"; path=/;";
        },

        /**
         * 문자열 hex 변환
         * @param str - 변환 대상 문자열
         * @returns 변환한 hex 값
         */
        toHex : function(str) {
            var hex = '';
            for(var i=0;i<str.length;i++) {
                hex += ''+str.charCodeAt(i).toString(16);
            }
            return hex;
        },

        /**
         * 문자열 hex 변환
         * @returns 오늘날짜(yyyy-mm-dd)
         */
        toDay : function(){
            var date = new Date();
            var year = date.getFullYear();
            var month = new String(date.getMonth()+1);
            var day = new String(date.getDate());

            // 한자리수일 경우 0을 채워준다.
            if(month.length == 1){
              month = "0" + month;
            }
            if(day.length == 1){
              day = "0" + day;
            }

            return year + "-" + month + "-" + day;
        },

        /**
         * 문자열 hex 변환
         * @returns 오늘날짜(yyyy-mm-dd)
         */
        lastWeek : function(){
            var date = new Date();

            date.setDate(date.getDate() - 7);

            var year = date.getFullYear();
            var month = new String(date.getMonth()+1);
            var day = new String(date.getDate());

            // 한자리수일 경우 0을 채워준다.
            if(month.length == 1){
                month = "0" + month;
            }
            if(day.length == 1){
                day = "0" + day;
            }

            return year + "-" + month + "-" + day;
        },

        /**
         * 말일 계산
         * @returns 말일날짜(yyyy-mm-dd)
         */
        lastMonth : function(){
            var date = new Date();

            date.setDate(date.getDate() - 30);

            var year = date.getFullYear();
            var month = new String(date.getMonth()+1);
            var day = new String(date.getDate());

            // 한자리수일 경우 0을 채워준다.
            if(month.length == 1){
                month = "0" + month;
            }
            if(day.length == 1){
                day = "0" + day;
            }

            return year + "-" + month + "-" + day;
        },

        /**
         * 과거 일 계산
         * @param name - 개월 수
         * @returns 말일날짜(yyyy-mm-dd)
         */
        preDay : function(cnt){
        	if(!$.isNumeric(cnt))cnt = 1;
        	
            var date = new Date();
            var year = date.getFullYear();
            var month = new String(date.getMonth()+1);
            var day = new String(date.getDate()-cnt);

            // 한자리수일 경우 0을 채워준다.
            if(month.length == 1){
                month = "0" + month;
            }
            if(day.length == 1){
                day = "0" + day;
            }

            return year + "-" + month + "-" + day;
        },

        /**
         * 과거 월 계산
         * @param name - 개월 수
         * @returns 말일날짜(yyyy-mm-dd)
         */
        // preMonth : function(cnt){
        // 	if(!$.isNumeric(cnt))cnt = 1;
        //
        //     var date = new Date();
        //     var year = date.getFullYear();
        //     var month = new String(date.getMonth()+1-cnt);
        //     var day = new String(date.getDate());
        //
        //     // 한자리수일 경우 0을 채워준다.
        //     if(month.length == 1){
        //         month = "0" + month;
        //     }
        //     if(day.length == 1){
        //         day = "0" + day;
        //     }
        //
        //     return year + "-" + month + "-" + day;
        // },
        preMonth: function (cnt) {
            if (!$.isNumeric(cnt)) {
                cnt = 1;
            }

            var date = new Date();
            var year = date.getFullYear();
            date.setMonth(date.getMonth() - cnt); // cnt 개월을 뺀다.
            var month = date.getMonth() + 1; // getMonth()는 0부터 시작하므로 1을 더한다.
            var day = date.getDate();

            year = date.getFullYear(); // cnt 개월을 뺀 후의 연도를 다시 가져온다.

            // 한자리수일 경우 0을 채워준다.
            month = month < 10 ? "0" + month : month;
            day = day < 10 ? "0" + day : day;

            return year + "-" + month + "-" + day;
        },


        /**
         * 파일업로드 확장자 체크
         * @param e - 이벤트 객체
         */
        handleImgFileSelect : function(e) {
            var files = e.target.files;
            var filesArr = Array.prototype.slice.call(files);

            var reg = /(.*?)\/(jpg|jpeg|png|bmp)$/;

            filesArr.forEach(function(f) {
                if (!f.type.match(reg)) {
                    _comLib.alertMsg("확장자는 이미지 확장자만 가능합니다.");
                    return;
                }

                sel_file = f;

                var reader = new FileReader();
                reader.onload = function(e) {
                    $(".noimg").hide();
                    $(".profile img").remove();
                    $(".profile").prepend("<img src=\""+e.target.result+"\">");
                }
                reader.readAsDataURL(f);
            })
        },

        /**
         * byte 파일 용량 환산 표출
         * @param bytes - 환산할 바이트 수
         * @returns 환산한 파일 용량
         */
        getFileSizeStr : function(bytes) {
            var bytes = parseInt(bytes);
            var dataType = ['bytes', 'Kb', 'Mb', 'Gb', 'Tb', 'Pb'];
            var dataChang = Math.floor(Math.log(bytes)/Math.log(1024));
            if(dataChang == "-Infinity"){
                return "0 "+dataType[0];
            } else {
                return (bytes/Math.pow(1024, Math.floor(dataChang))).toFixed(2)+" "+dataType[dataChang];
            }
        },

        /**
         * 특정 영역 인쇄
         * @param targetId - 인쇄 대상 객체ID
         */
        print : function(targetId) {
            var contents = $("#"+targetId).html();
            var printFrame = $("<iframe name='printFrame' style='position:absolute; top:-1000000px;' />");
            var frameDoc;

            // 인쇄 영역을 바디에 생성
            $("body").append(printFrame);

            frameDoc = printFrame[0].contentWindow ? printFrame[0].contentWindow : printFrame[0].contentDocument.document ? printFrame[0].contentDocument.document : printFrame[0].contentDocument;
            frameDoc.document.open();

            // 타이틀 생성
            frameDoc.document.write('<html><head><title>안전톡</title>');
            frameDoc.document.write('</head><body>');

            // CSS 추가
            frameDoc.document.write('<link rel="stylesheet" href="/css/design.css">');
            frameDoc.document.write('<link rel="stylesheet" href="/css/design_custom.css">');
            frameDoc.document.write('<link rel="stylesheet" href="/css/paging.css">');
            frameDoc.document.write('<link rel="stylesheet" href="/css/jquery-ui.css">');

            // 내용 추가
            frameDoc.document.write(contents);
            frameDoc.document.write('</body></html>');
            frameDoc.document.close();

            // 인쇄 처리(즉시 호출하면 내용이 안보일수 있어서 딜레이 처리)
            setTimeout(function () {
                window.frames["printFrame"].focus();
                window.frames["printFrame"].print();
                printFrame.remove();
            }, 500);
        },

        /**
         * 특정 영역 PDF 생성
         * @param targetId - PDF 생성 대상 객체ID
         * @param fileName - 다운로드 파일명
         */
        selectCodeList : function(grpCd) {
	        // Json방식으로 전송하여 리턴 값 받음.
			$.post("/common/selectCodeList", {"grpCd":grpCd}, function(data){
				console.log(list);
			}, "json");
        },
        

        /**
         * 특정 영역 PDF 생성
         * @param targetId - PDF 생성 대상 객체ID
         * @param fileName - 다운로드 파일명
         */
        makePdf : function(targetId, fileName) {
            html2canvas($("#"+targetId)[0]).then(function(canvas) {

                var doc = new jsPDF("p", "mm", "a4"); // jspdf객체 생성
                var imgData = canvas.toDataURL("image/png"); // 캔버스를 이미지로 변환

                // 이미지를 기반으로 pdf생성
                doc.addImage(imgData, "PNG", 0, 0);

                // PDF 저장
                if(typeof(fileName) != "undefined") {
                    doc.save(fileName+"pdf");
                } else {
                    doc.save("safetytalk.pdf");
                }
            });
        }
    }
})();