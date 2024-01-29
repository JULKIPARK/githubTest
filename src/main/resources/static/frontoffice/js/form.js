
//---------------------------------------------------------------------------
// 공통 기능
//---------------------------------------------------------------------------
var _formLib = (function(){

    // 내부 변수(private) 영역

    return {

        /**
         * 입력 폼 초기화
         * @param obj - 대상 객체
         */
        reset: function(obj) {
            $("#"+obj).closest('form').find('input[type="text"]').val('');
            $("#"+obj+" select").find('option:first').prop('selected', true);
        },

        /**
         * 널 체크 함수 - null, undefined
         * @param obj - 대상 객체
         * @returns boolean
         */
        isEmpty : function(obj) {
            return (typeof obj !="undefined" && obj != null && obj != "null" && obj !="") ? false : true;
        },

        /**
         * 값 조회
         * @param obj - 대상 객체
         * @returns 조회 값(null 일 경우 공백"" 반환)
         */
        getValue : function(obj) {
            return typeof(obj) == "undefined" ? "" : obj == null ? "" : obj == "null" ? "" : obj;
        },

        /**
         * 폼 데이터 조회
         * @param target - 대상 객체ID
         * @returns 폼 데이터(json)
         */
        getFormData : function(target) {
            var formData = {};
            var objList = $("#"+target).find("input, select, textarea");

            // 폼 데이터 추출
            $.each(objList, function(index, item) {
                if($(item).prop("name") != "") {

                    // 태그 유형에 따른 조회 처리
                    if(typeof($(item).prop("type")) != undefined && $(item).prop("type").toUpperCase() == "RADIO" && typeof(formData[$(item).prop("name")] == "undefined")) {
                        formData[$(item).prop("name")] = $("input[type=radio][name="+$(item).prop("name")+"]:checked").val();
                    } else if (typeof($(item).prop("type")) != undefined && $(item).prop("type").toUpperCase() == "CHECKBOX" && typeof(formData[$(item).prop("name")] == "undefined")) {
                        var checkedVal = "";

                        // 체크값 설정
                        $.each($("input[type=checkbox][name="+$(item).prop("name")+"]:checked"), function(chkIndex, chkItem){
                            if(checkedVal != "") {
                                checkedVal += ",";
                            }
                            checkedVal += $(chkItem).val();
                        });

                        formData[$(item).prop("name")] = checkedVal;
                    } else {

                        // 번호에 들어간 ('-') 하이픈 제거
                        if($(item).prop("id").toUpperCase().indexOf("TELNO") > 0 || $(item).prop("id").toUpperCase().indexOf("DATE") > 0) {
                            formData[$(item).prop("name")] = $(item).val().replace(/\-/g,"");
                        } else {
                            formData[$(item).prop("name")] = $(item).val();
                        }
                    }
                }
            });

            return formData;
        },

        /**
         * 폼 데이터 설정
         * @param target - 대상 객체ID
         * @param jsonData - 설정할 데이터
         * @param excludeTarget - 예외 대상 객체ID
         */
        setFormData : function(target, jsonData, excludeTarget) {
            var keyList = Object.keys(jsonData);

            // 예외 대상 여부에 따른 대상 설정
            if(typeof(excludeTarget) != "undefined") {
                keyList = keyList.filter(o => o !== excludeTarget);
            }

            $.each(keyList, function(index, item){
                var obj = $("#"+target).find("input[name="+item+"],select[name="+item+"],textarea[name="+item+"]").eq(0);

                if(obj.length > 0) {

                    // 태그 유형에 따른 조회 처리
                    if(typeof($(obj).prop("type")) != undefined && $(obj).prop("type").toUpperCase() == "RADIO") {
                        $("#"+target).find("input[type=radio][name="+item+"][value='"+jsonData[item]+"']").prop("checked", true);
                    } else if(typeof($(obj).prop("type")) != undefined && $(obj).prop("type").toUpperCase() == "CHECKBOX") {
                        $.each(jsonData[item].trim().split(","), function(valIndex, valItem){
                            $("#"+target).find("input[type=checkbox][name="+item+"][value='"+valItem+"']").prop("checked", true);
                        });
                    } else {
                        if(typeof($(obj).prop("type")) != undefined && $(obj).prop("type").toUpperCase() == "SELECT-ONE") {
                            $("#"+target).find($(obj).prop("tagName").toLowerCase()+"[name="+item+"]").val(jsonData[item]).trigger("change");
                        } else {
                            $("#"+target).find($(obj).prop("tagName").toLowerCase()+"[name="+item+"]").val(jsonData[item]);
                        }
                    }
                }
            });

        },

        /**
         * 폼 데이터 초기화
         * @param target - 대상 객체ID
         */
        initFormData : function(target) {
            var objList = $("#"+target).find("input, select, textarea");

            $.each(objList, function(index, item){

                if($(item).prop("name") != "") {

                    // 태그 유형에 따른 초기화 처리
                    if($(item).prop("type").toUpperCase() == "RADIO" || $(item).prop("type").toUpperCase() == "CHECKBOX") { // RADIO

                        // 체크/라디오박스에 default 옵션이 있을 항목은 체크 상태로 초기화
                        if($(item).attr("default") != undefined && $(item).attr("default") == "Y") {
                            $(item).prop("checked",true);
                        } else {
                            $(item).prop("checked",false);
                        }

                    } else { // INPUT, SELECT, TEXTAREA
                        if($(item).prop("tagName") == "SELECT") {
                            $(item).find('option:first').prop('selected', true);
                        } else {
                            $(item).val("");
                        }
                    }
                }
            });
        },

        /**
         * 입력 항목 활성/비활성
         * @param target - 대상 객체ID
         * @param flag - 비활성 여부(true : 비활성, false : 활성)
         * @param excludeTarget - 예외 대상 객체ID
         */
        setDisable : function(target, flag, excludeTarget) {
            var objList = $(/^(#|\.)/.test(target) != true ? "#"+target : target).find("input, select, textarea");
            var fnBlockEvent = function(e){
                e.stopPropagation();
                return false;
            };

            // 예외 대상 여부에 따른 대상 설정
            if(typeof(excludeTarget) != "undefined") {
                objList = $(objList).not(/^(#|\.)/.test(excludeTarget) != true ? "#"+excludeTarget : excludeTarget);
            }

            $.each(objList, function(index, item){
                if($(item).prop("type").toUpperCase() == "RADIO" || $(item).prop("type").toUpperCase() == "CHECKBOX") {
                    if(flag == true) {
                        $(item).bind("click", fnBlockEvent);
                    } else {
                        $(item).unbind("click");
                    }
                } else {
                    $(item).attr("disabled", flag);
                }
            });
        },

        /**
         * 콤보박스 항목을 유효한 값(USE_YN:Y)으로만 설정
         * @param target - 대상 객체ID
         * @param excludeTarget - 예외 대상 객체ID
         */
        setComboDataValid : function(target, excludeTarget) {
            var objList = $(/^(#|\.)/.test(target) != true ? "#"+target : target).find("input[type=radio], select");
            var ifnDelOption = function(obj) {
                var type = $(obj).prop("type").toUpperCase();
                var selectedVal;

                // 콤보박스 유형별 처리
                if(type.indexOf("SELECT") != -1) { // SELECT
                    selectedVal = $(obj).children("option:selected").val();

                    // 선택된 값이 아닌데, 사용여부가 N인 항목은 삭제
                    $(obj).children("option").each(function(){
                        if($(this).val() != selectedVal && $(this).attr("useYn") == "N") {
                            $(this).remove();
                        }
                    });
                } else if(type.indexOf("RADIO") != -1){ // RADIO
                    var objId = $(obj).prop("id");

                    // 선택된 값이 아닌데, 사용여부가 N인 항목은 삭제
                    if($(obj).is(":checked") != true && $(obj).attr("useYn") == "N") {

                        // 해당 객체와 레이블을 같이 삭제
                        $(obj).remove();
                        $("label[for="+objId+"],span[for="+objId+"],i[for="+objId+"]").remove();
                    }
                }
            }

            // 예외 대상 여부에 따른 대상 설정
            if(typeof(excludeTarget) != "undefined") {
                objList = $(objList).not(/^(#|\.)/.test(excludeTarget) != true ? "#"+excludeTarget : excludeTarget);
            }

            // 콤보박스에서 유효하지 않은 값 삭제
            $.each(objList, function(index, item) {
                ifnDelOption(item);
            });
        },

        /**
         * 입력 폼 초기화
         * @param target - 대상 객체
         * @param type - 대상 객체
         * @returns 조회 값(null 일 경우 공백"" 반환)
         */
        resetLayer : function(target, type) {
            if(type == 'form'){
                $("#"+target).closest('form').find('input[type="text"]').val('');
                $("#"+target+" select").find('option:first').prop('selected', true);
                //$("#"+obj).closest('form').find('input[type="checkbox"]').prop('checked', false);
            }else if(type == 'cmmnPop'){
                $("#"+target).find('ul').empty();
                $("#"+target).find('span').text('');
                $("#"+target).find('input[type="text"]').val('');
            } else {
                $("#"+target).find('input[type="text"]').val('');
                $("#"+target).find('textarea').val('');
                $("#"+target+" select").find('option:first').prop('selected', true);
            }
        },

        /**
         * 입력 폼 유효성 체크
         * @returns boolean
         */
        isValid: function(target) {
            var validationChk = true;

            $("#"+target+" .required").each(function(){
                var value = $(this).val();
                var dataYn = $(this).attr("data-yn");

                // 널 체크
                if($(this).hasClass("nvlChk")){
                    var skippable = false;

                    // 필수가 되기 위한 선행 조건이 있는지 체크
                    if(typeof($(this).attr("requiredBy")) != "undefined") {
                        var requiredInfo = $(this).attr("requiredBy").split(":");
                        var requiredObjType = $("#"+target+" input[name="+requiredInfo[0]+"]").eq(0).prop("type").toUpperCase();
                        var requiredObjValue = "";

                        if(requiredObjType == "RADIO") {
                            requiredObjValue = $("#"+target+" input[name="+requiredInfo[0]+"]:checked").val();
                        } else if (requiredObjType == "SELECT") {
                            requiredObjValue = $("#"+target+" select[name="+requiredInfo[0]+"]:checked").val();
                        }

                        // 선행 조건이 충족할 경우, 필수체크 항목으로 인식
                        if(requiredObjValue != requiredInfo[1]) {
                            skippable = true;
                        }
                    }

                    // 라디오박스일 경우 선택된 값으로 체크
                    if($(this).prop("type").toUpperCase() == "RADIO" && typeof($(this).prop("name")) != "undefined") {
                        value = $("#"+target+" input[type=radio][name="+$(this).prop("name")+"]:checked").val();

                        console.log ("RADIO ======= "+ $.trim(value));
                    }

                    // 체크박스일 경우 선택된 값 길이로 체크
                    if($(this).prop("type").toUpperCase() == "CHECKBOX" && typeof($(this).prop("name")) != "undefined") {
                        value = $("#"+target+" input[type=checkbox][name="+$(this).prop("name")+"]:checked").length <= 0 ? "" : "true";
                    }

                    //부서관리코드 추출
                    var dprtClssCd = "";

                    if($("select[name=dprtClssCd]").val() != undefined){
                        dprtClssCd = $("#"+target+" select[name=dprtClssCd]").val();
                    }

                    if(!_formLib.isEmpty(dprtClssCd)){
                        var targetNm= $(this).prop("name");
                        var selectVal = "";

                        if(targetNm == "hdqrCd"){
                            selectVal = $("#"+target+" select[name="+targetNm+"]").val();
                            if(selectVal == "-"){
                                value = "";
                            }
                        }
                    }

                    if("" == $.trim(value) && skippable == false){
                        _comLib.alertMsg($(this).attr("title")+"을(를) 입력해 주세요.");
                        $(this).focus();
                        validationChk = false;
                        return false;
                    }
                }

                if(_formLib.isEmpty(dataYn) || dataYn == 'y'){
                    // 아이디 체크
                    if($(this).hasClass("idChk")){
                        var regExp = /^[A-Za-z0-9]{6,12}$/;
                        if(!regExp.test(value)){
                            _comLib.alertMsg($(this).attr("title")+"는 4~20자의 영문, 숫자만 사용 가능합니다.");
                            $(this).focus();
                            validationChk = false;
                            return false;
                        }
                    }

                    // 숫자 체크 (소수점 가능)
                    if($(this).hasClass("numChk")){
                        if(isNaN(value)){
                            _comLib.alertMsg($(this).attr("title")+"은 숫자를 입력해 주세요. 예) 37.5693");
                            $(this).focus();
                            validationChk = false;
                            return false;
                        }
                    }

                    // 정수 체크
                    if($(this).hasClass("intChk")){
                        if(Number.isInteger(Number(value)) != true){
                            _comLib.alertMsg($(this).attr("title")+"은 소수점이 없는 숫자만 입력해 주세요. 예) 37");
                            $(this).focus();
                            validationChk = false;
                            return false;
                        }
                    }

                    // 사업자 등록번호 체크
                    if($(this).hasClass("bizChk") && !_formLib.isEmpty(value)){
                        var valueMap = value.replace(/-/gi, '').split('').map(function(item) {
                            return parseInt(item, 10);
                        });

                        if (valueMap.length === 10) {
                            var multiply = new Array(1, 3, 7, 1, 3, 7, 1, 3, 5);
                            var checkSum = 0;
                            var flag;

                            for (var i = 0; i < multiply.length; ++i) {
                                checkSum += multiply[i] * valueMap[i];
                            }

                            checkSum += parseInt((multiply[8] * valueMap[8]) / 10, 10);

                            flag = Math.floor(valueMap[9]) === (10 - (checkSum % 10));

                            if(flag){ //사업자번호 검증 성공
                                return true;
                            } else { //사업자번호 검증 실패
                                _comLib.alertMsg($(this).attr("title")+"는 형식에 맞춰 입력해 주세요. 예) 000-00-00000");
                                $(this).focus();
                                validationChk = false;
                                return false;
                            }
                        } else {
                            _comLib.alertMsg($(this).attr("title")+"는 형식에 맞춰 입력해 주세요. 예) 000-00-00000");
                            $(this).focus();
                            validationChk = false;
                            return false;
                        }
                    }

                    //법인 등록번호 체크
                    if($(this).hasClass("regNoChk")){
                        var regExp = /^(\d{6,6})-?(\d{7,7})/i;
                        if(!regExp.test(value)){
                            _comLib.alertMsg($(this).attr("title")+"는 형식에 맞춰 입력해 주세요. 예) 000000-0000000");
                            $(this).focus();
                            validationChk = false;
                            return false;
                        }
                    }

                    // 이메일 체크
                    if($(this).hasClass("emailChk") && !_formLib.isEmpty(value)){
                        var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
                        if(!regExp.test(value)){
                            _comLib.alertMsg($(this).attr("title")+"은 형식에 맞춰 입력해 주세요. 예) gildong@naver.co.kr");
                            $(this).focus();
                            validationChk = false;
                            return false;
                        }
                    }

                    // 전화번호 체크
                    if($(this).hasClass("phoneChk") && !_formLib.isEmpty(value)){
                        var fstNo = value.substring(0,2);
                        var regExp = "";

                        if(fstNo == '02'){
                            regExp = /^(02)-[0-9]{3,4}-[0-9]{4}$/i;

                            if(!regExp.test(value)){
                                _comLib.alertMsg($(this).attr("title")+"은 형식에 맞춰 입력해 주세요. 예) 000-0000-0000");
                                $(this).focus();
                                validationChk = false;
                                return false;
                            }
                        } else if(fstNo != '02'){
                            regExp = /^\d{3}-\d{3,4}-\d{4}$/i;

                            if(!regExp.test(value)){
                                _comLib.alertMsg($(this).attr("title")+"은 형식에 맞춰 입력해 주세요. 예) 000-0000-0000");
                                $(this).focus();
                                validationChk = false;
                                return false;
                            }
                        } else {
                            _comLib.alertMsg($(this).attr("title")+"은 형식에 맞춰 입력해 주세요. 예) 000-0000-0000");
                            $(this).focus();
                            validationChk = false;
                            return false;
                        }
                    }

                    //위도 체크
                    if("pos_lat" == $.trim($(this).attr("id")) && !_formLib.isEmpty(value)){
                        if(33.11111111 > Number(value) || Number(value) > 43.00972222) {
                            _comLib.alertMsg($(this).attr("title")+"은 대한민국의 범위를 벗어납니다.");
                            $(this).focus();
                            validationChk = false;
                            return false;
                        }
                    }

                    // 경도 체크
                    if("pos_lon" == $.trim($(this).attr("id")) && !_formLib.isEmpty(value)){
                        if(124.19583333 > Number(value) || Number(value) > 131.87222222){
                            _comLib.alertMsg($(this).attr("title")+"은 대한민국의 범위를 벗어납니다.");
                            $(this).focus();
                            validationChk = false;
                            return false;
                        }
                    }

                    // 금액 체크
                    if($(this).hasClass("priceChk")){ //금액
                        var priceValue = value.replace(/,/g, "");
                        var regExp = /^(\d*)[\.]?(\d{1,3})?$/g;

                        if(regExp.test(Number(priceValue)) !=  true){
                            _comLib.alertMsg($(this).attr("title")+"은 특수기호(,)와 소수점 3자리까지만 사용 가능합니다.");
                            $(this).focus();
                            validationChk = false;
                            return false;
                        }
                    }

                    // 길이 체크
                    if($(this).hasClass("lengthChk") && $(this).attr("length") != ""){ //금액
                        var valueLength = value.length;
                        var maxLength = $(this).attr("length");

                        if(valueLength > maxLength){
                            _comLib.alertMsg($(this).attr("title")+"의 글자 길이("+valueLength+")는 "+maxLength+"보다 작아야 합니다.");
                            $(this).focus();
                            validationChk = false;
                            return false;
                        }
                    }

                    // 최대이륙중량 체크
                    if($(this).hasClass("maxTkoWigtChk")){
                        var maxTkoWigtChk = value.replace(/,/g, "");
                        var regExp = /^[0-9]{1,3}(\.[0-9]{1,3})?$/g;

                        if(regExp.test(Number(maxTkoWigtChk)) !=  true){
                            _comLib.alertMsg($(this).attr("title")+"은 정수 최대 3자리, 소수 3자리까지만 사용 가능합니다.");
                            $(this).focus();
                            validationChk = false;
                            return false;
                        }
                    }

                    // 내풍성 체크
                    if($(this).hasClass("wdrcValChk")){
                        var wdrcVal = value.replace(/,/g, "");
                        var regExp = /^[0-9]{1,3}(\.[0-9]{1})?$/g;

                        if(regExp.test(Number(wdrcVal)) !=  true){
                            _comLib.alertMsg($(this).attr("title")+"은 정수 최대 2자리, 소수 1자리까지만 사용 가능합니다.");
                            $(this).focus();
                            validationChk = false;
                            return false;
                        }
                    }
                }
            });

            return !validationChk ? false : true;
        },

        /**
         * 시간 유효성 체크
         * @param stObjId - 시작시간ID
         * @param edObjId - 종료시간ID
         * @returns 유효성 여부(true:유효)
         */
        checkTimeValid: function(stObjId, edObjId) {
            var timeFormat = /^([01][0-9]|2[0-3])([0-5][0-9])$/;
            var rtnData = {
                "isValid" : true
            };
            var stTime = $("#"+stObjId).val().replace(":", "");
            var edTime = $("#"+edObjId).val().replace(":", "");

            if (_formLib.isEmpty(stTime) && _formLib.isEmpty(edTime)) {
                rtnData.isValid = false;
                rtnData.invalidObjId = stObjId;
                rtnData.msg = "시작시간 및 종료시간을 입력해 주세요.";
                return rtnData;
            }

            if (_formLib.isEmpty(stTime)) {
                rtnData.isValid = false;
                rtnData.invalidObjId = stObjId;
                rtnData.msg = "시작시간을 입력해 주세요.";
                return rtnData;
            }

            if (_formLib.isEmpty(edTime)) {
                rtnData.isValid = false;
                rtnData.invalidObjId = edObjId;
                rtnData.msg = "종료시간을 입력해 주세요.";
                return rtnData;
            }

            // 시작시간 형식 체크
            if (rtnData.isValid == true && stTime != "" && timeFormat.test(stTime) != true) {
                rtnData.isValid = false;
                rtnData.invalidObjId = stObjId;
                rtnData.msg = "시작시간은 hh:mm 형태로 입력해 주세요. 예) 15:35";
                return rtnData;
            }

            // 종료시간 형식 체크
            if (rtnData.isValid == true && edTime != "" && timeFormat.test(edTime) != true) {
                rtnData.isValid = false;
                rtnData.invalidObjId = edObjId;
                rtnData.msg = "종료시간은 hh:mm 형태로 입력해 주세요. 예) 15:35";
                return rtnData;
            }

            // 시작시간이 종료시간보다 큰지 체크
            /*
            if (rtnData.isValid == true && stTime != "" && edTime != "") {
                var stTimStr = $("#"+stObjId).val().split(":")[0];
                var edTimetr = $("#"+edObjId).val().split(":")[0];
                var stTimeNum = Number(stTimStr);
                var edTimeNum = Number(edTimetr);
                // 날짜를 숫자형태의 날짜 정보로 변환하여 비교
                if (stTimeNum > edTimeNum) {
                    rtnData.isValid = false;
                    rtnData.invalidObjId = stObjId;
                    rtnData.msg = "종료시간보다 시작시간이 작아야 합니다.";
                }
            }
            */

            if(rtnData.isValid){
                rtnData.stTime = stTime;
                rtnData.edTime = edTime;
            }

            return rtnData;
        },

        /**
         * 시작/종료 기간의 일수/달수 조회
         * @param stObjId - 시작시간ID
         * @param edObjId - 종료시간ID
         * @param type - 계산 유형(DAY:일단위, MONTH:월단위, YEAR:년단위)
         */
        getDateTerm : function(stObjId, edObjId, type) {
            var stDateAry = $("#"+stObjId).val().split("-");
            var edDateAry = $("#"+edObjId).val().split("-");
            var rtnData;

            if(type == "DAY") { // 일단위
                var stDate = new Date(stDateAry[0], Number(stDateAry[1])-1, stDateAry[2]);
                var edDate = new Date(edDateAry[0], Number(edDateAry[1])-1, edDateAry[2]);

                rtnData = Math.floor((edDate.getTime() - stDate.getTime()) / (1000*60*60*24)) + 1;
            } else if (type == "MONTH") { // 월단위

                // 배열의 값을 숫자로 변환
                stDateAry = stDateAry.map(function(data){return Number(data);});
                edDateAry = edDateAry.map(function(data){return Number(data);});

                if(stDateAry[0] == edDateAry[0]) {
                    rtnData = edDateAry[1] - stDateAry[1] + 1;
                } else {
                    rtnData = ((edDateAry[0]-stDateAry[0])*12) + edDateAry[1] - stDateAry[1] + 1;
                }

            } else if (type == "YEAR") { // 년단위
                rtnData = Number(edDateAry[0]) - Number(stDateAry[0]);
            }

            return rtnData;
        },

        /**
         * 피트값을 미터값으로 변환
         * @param feetVal - 미터값
         */
        feetToMeter : function(feetVal) {
            var meterVal = 0;

            meterVal = feetVal * 0.3048;
            return meterVal;
        },

        /**
         * 미터값을 피트값으로 변환
         * @param meterVal - 미터값
         */
        meterToFeet : function(meterVal) {
            var feetVal = 0;

            feetVal = meterVal / 0.3048;
            return feetVal;
        },

        /**
         *천단위 콤마 펑션
         * @param value - 변환 할 값
         * @returns 변환 결과
         */
        addComma : function(value) {
	        value = value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	        return value; 
        },

        /**
         * 체크박스 전체 선택/해제
         * @param obj - 이벤트 객체
         */
        setCheckAll: function(obj) {
        	$("input:checkbox[name="+$(obj).prop("name")+"]").prop('checked', $(obj).prop('checked'));
        },

        /**
         * 체크박스 전체 선택/해제
         * @param obj - 이벤트 객체
         */
        setAllCheck: function(obj) {
			var isAll = $("input:checkbox[name="+$(obj).prop("name")+"]").eq(0).prop('checked');
			var totalCnt = $("input:checkbox[name="+$(obj).prop("name")+"]").length-1;
			var checkedCnt = $("input:checkbox[name="+$(obj).prop("name")+"]:checked").length;
			checkedCnt = isAll?checkedCnt-1:checkedCnt;
			
			if(totalCnt==checkedCnt){
				$("input:checkbox[name="+$(obj).prop("name")+"]").eq(0).prop('checked', true);
			}
			
			else{
				$("input:checkbox[name="+$(obj).prop("name")+"]").eq(0).prop('checked', false);
			}
			
        }
    }
})();