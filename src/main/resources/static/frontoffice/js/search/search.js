/* 페이지 선택 */
function gotopage(p) {	
	$('#page').val(p);
	$('#pageNum').val((p+1));
	
	//페이지 번호 활성화
	var pageNum = $('#pageNum').val();
	
	$('#historyForm').submit();
}

/* 정렬 */
function sorting(val) {
	$('#sort').val(val);
	
	var sort = $('#sort').val();
	env_form(); //esg 키워드 값
	
	$('#historyForm').submit();
}



/* 카테고리 선택 */
function goCategory(val) {
	$('#category').val(val); //카테고리 값 매핑
	$('#pageNum').val(1); // pageNum 초기화
	
	$('#date').val(''); //date 초기화
	$('#startDate').val(''); //startDate 초기화
	$('#endDate').val(''); //endDate 초기화
	
	$('#dmstc_yn').val('');
	$('#sply_lang_cd').val('');
	$('#pblcn_yy').val('');
	$('#vod_qunty_mi').val('');
	$('#sbjt_gb_nm').val('');
	$('#task_clsf_nm').val('');
	$('#env').val('');
	$('#esgKwdList').val('');
	

	$('#historyForm').submit();

}


/* 통합검색 */
function submitMain(){
	$('#category').val('TOTAL');
	$('#sort').val('pd');	
	submitkwd();
}

/* 삼일 인사이트, esg 자료실, esg 뉴스 검색 */
function submitOther(){
	$('#sort').val('r');
	var emptyKwd = $('#search_kwd').val();
	if(emptyKwd == null || emptyKwd == ""){
		$('#sort').val('pd');
	}
	submitkwd();
}

/* 기본검색 */
function submitkwd(){
	//검색키워드 저장
	var kwd = $('#search_kwd').val();
	$('#kwd').val(kwd);
	
	
	$('#historyForm').submit();
}



/* 연관 검색 */
function recommandKwd(buttonElement) {
	var kwd = buttonElement.innerText || buttonElement.textContent;
	kwd = kwd.replace(/\s/g, "").trim();
	
	$('#kwd').val(kwd);
	$('#category').val('TOTAL');
	$('#sort').val('r'); //검색 시는 무조건 정확도 순 정렬 노출
	
	$('#historyForm').submit();
	
}

/* 필드 검색 */
function click_field (buttonElement) {
	var field_id = buttonElement.getAttribute('id');
	
	$('#fields').val(field_id);
}







/* esgNewsList에서 buttton 으로 구현된 날짜 검색 */
function click_date (buttonElement) {
	var date_id = buttonElement.getAttribute('id');
	
	
	var env = [];
	$("input:checkbox[name=esgKwdBox]:checked").each(function() {
	    var envStr = $(this).val();
	    env.push(envStr);
	});
	
	$('#env').val(env);
	
	
	
	var currentDate = new Date();
	
	if(date_id == "esg_1w") {
		//최근 1주일
		var oneWeekAgo = new Date(currentDate);
   	 	oneWeekAgo.setDate(currentDate.getDate() - 7);
    	setStartDateAndEndDate(oneWeekAgo, currentDate);
    	$('#date').val(date_id);
    	$('#historyForm').submit();
	}
	if(date_id == "esg_1m") {
		//최근 1개월
		var oneMonthAgo = new Date(currentDate);
    	oneMonthAgo.setMonth(currentDate.getMonth() - 1);
    	setStartDateAndEndDate(oneMonthAgo, currentDate);
    	$('#date').val(date_id);
    	$('#historyForm').submit();
	}
	if(date_id == "esg_3m") {
		//최근 3개월
		var oneMonthAgo = new Date(currentDate);
    	oneMonthAgo.setMonth(currentDate.getMonth() - 3);
    	setStartDateAndEndDate(oneMonthAgo, currentDate);
    	$('#date').val(date_id);
    	$('#historyForm').submit();
	}
	if(date_id == "esg_6m") {
		//최근 6개월
		var oneMonthAgo = new Date(currentDate);
    	oneMonthAgo.setMonth(currentDate.getMonth() - 6);
    	setStartDateAndEndDate(oneMonthAgo, currentDate);
    	$('#date').val(date_id);
    	$('#historyForm').submit();
	}
	
}



// 시작 날짜와 종료 날짜를 설정하는 함수
function setStartDateAndEndDate(startDate, endDate) {
	// 날짜 형식을 "YYYYMMDD"로 변환
	var formattedStartDate = formatDate(startDate);
	var formattedEndDate = formatDate(endDate);
	
	// 시작 날짜와 종료 날짜 설정
	$('#startDate').val(formattedStartDate);
	$('#endDate').val(formattedEndDate);
}

// formatDate 함수 정의
function formatDate(date) {
    var year = date.getFullYear();
    var month = String(date.getMonth() + 1).padStart(2, '0');
    var day = String(date.getDate()).padStart(2, '0');
    return year + month + day;
}






/* 필터 리셋 */
function resetFilter() {
	$('#dmstc_yn').val('');
	$('#sply_lang_cd').val('');
	$('#pblcn_yy').val('');
	$('#vod_qunty_mi').val('');
	$('#sbjt_gb_nm').val('');
	$('#task_clsf_nm').val('');
	$('#env').val('');
	$('#esgKwdList').val('');
	$("input[type='checkbox'][name='esgKwdBox']").each(function() {
		$(this).prop('checked', false);
	});

	$('#historyForm').submit();
}



/* insightFilter - ESG 키워드 */
function ESG_Kwd (val) {
	$('#env').val(''); //필드 초기화
	$('#esgKwdList').val('');
	
	var esgKwdArr = Array.from({ 0: "env_list", 1: "so_list", 2: "gov_list",
				 					3: "fin_list", 4 : "dis_list" , 5: "tax_list", 
				 					6: "deal_list", length: 7 });
	
	
	var esgClassArr = Array.from({ 0: "env_btn", 1: "so_btn", 2: "gov_btn",
				 					3: "fin_btn", 4 : "dis_btn" , 5: "tax_btn", 
				 					6: "deal_btn", length: 7 });
	
	
	for(let i=0; i<=6; i++) {
		if(val == esgKwdArr[i]) {
			document.getElementById(eval("'" + val + "'")).style.display = "block";
			
			$('#' + esgClassArr[i]).addClass('current');
			$('#esgKwdList').val(esgKwdArr[i]);
			
		}else {
			document.getElementById(eval("'" + esgKwdArr[i] + "'")).style.display = "none";
			$('#' + esgClassArr[i]).removeClass('current');		
		}
	}
	
	
	
}


//ESG 주제분류 클릭시 check 해제
function ESG_Kwd_click (val) {
	$('#env').val(''); //필드 초기화
	$('#esgKwdList').val('');
	
	var esgKwdArr = Array.from({ 0: "env_list", 1: "so_list", 2: "gov_list",
				 					3: "fin_list", 4 : "dis_list" , 5: "tax_list", 
				 					6: "deal_list", length: 7 });
	
	
	var esgClassArr = Array.from({ 0: "env_btn", 1: "so_btn", 2: "gov_btn",
				 					3: "fin_btn", 4 : "dis_btn" , 5: "tax_btn", 
				 					6: "deal_btn", length: 7 });
	
	
	for(let i=0; i<=6; i++) {
		if(val == esgKwdArr[i]) {
			document.getElementById(eval("'" + val + "'")).style.display = "block";
			
			$('#' + esgClassArr[i]).addClass('current');
			$('#esgKwdList').val(esgKwdArr[i]);
			$("input[type='checkbox'][name='esgKwdBox']").each(function() {
				$(this).prop('checked', false);
			});
		}else {
			document.getElementById(eval("'" + esgKwdArr[i] + "'")).style.display = "none";
			$('#' + esgClassArr[i]).removeClass('current');		
		}
	}
	
	
	
}


/* ESG키워드 - 환경 */
function esgKwdFilter() {
	
	$('#env').val(''); //필드 초기화
	//$('#esgKwdList').val(''); //필드 초기화

	var env = [];
	$("input:checkbox[name=esgKwdBox]:checked").each(function() {
	    var envStr = $(this).val();
	    env.push(envStr);
	});
	
	$('#env').val(env);
	
	$('#historyForm').submit();
	
}



function env_form() {
	var env = [];
	$("input:checkbox[name=esgKwdBox]:checked").each(function() {
	    var envStr = $(this).val();
	    env.push(envStr);
	});
	
	$('#env').val(env);
}

	






$(document).ready(function() {
	
	/* 카테고리 언더바 활성화 */
	var cate = $('#category').val();
	cate = '#'+cate;
	$(cate).addClass('current');
	
	/* 최신순 등 정렬 활성화 */
	var sort = $('#sort').val();
	sort = '#sort_' + sort;
	$(sort).addClass('current');
	
	
	/* 필터 */		
	//ESG 키워드
	esgKwdCheckedStates();
	
	var esgKwdList = $('#esgKwdList').val();
	
	if(esgKwdList != ""){
		ESG_Kwd(esgKwdList);
	}else{
		esgKwdList = 'env_list';
		ESG_Kwd(esgKwdList);
	}
	 
	//ESG 주제분류
	$("input[type='checkbox'][data-filter='sbjt']").change(function() {
		sbjtCheckedValues();
	});
	sbjtCheckedStates(); 
	 
	 
	// ESG 업무분류
	$("input[type='checkbox'][data-filter='task']").change(function() {
		taskCheckedValues();
	});
	taskCheckedStates(); 
	 
	
	//통합검색 문서 - 자료 출처
	$("input[type='checkbox'][data-filter='dmstc']").change(function() {
		dmstcCheckedValues();
	});
	dmstcCheckedStates();
	
	
	//통합검색 문서 - 언어
	$("input[type='checkbox'][data-filter='lang']").change(function() {
		langCheckedValues();
	});
	langCheckedStates();
	
	
	//문서 - 발간연도
	$("input[type='checkbox'][data-filter='years']").change(function() {
		sendCheckedValues();
	});
	restoreCheckedStates();
	
	
	// 미디어 - 영상분량
	$("input[type='checkbox'][data-filter='media']").change(function() {
		mediaCheckedValues();
	});
	mediaCheckedStates();
	
	
	 
	return;
});


	//esg키워드
	function esgKwdCheckedStates() {
		
		var esgKwdValue = $("#env").val();
    	if (esgKwdValue) {
	        var esgKwd = esgKwdValue.split(',');
	       	
			$("input[type='checkbox'][name='esgKwdBox']").each(function() {
	            var savedEsgKwd = $(this).val();
	            if (esgKwd.includes(savedEsgKwd)) {
	            	$(this).prop('checked', true);
	        	} else {
	            	$(this).prop('checked', false);
	        	}
	        });
		}
    }

	//통합검색 - 자료출처
	function dmstcCheckedValues(){
		var checkDmstc = [];
		$("input[type='checkbox'][data-filter='dmstc']:checked").each(function() {
        // 체크박스 바로 옆에 있는 라벨에서 년도 값 추출
        var dmstc = $(this).val();
        checkDmstc.push(dmstc);
    	});
    	$('#dmstc_yn').val(checkDmstc);
    	env_form();
    	$('#pageNum').val(1);
    	$('#historyForm').submit();
	}
	
  
    function dmstcCheckedStates() {
		var dmstcValue = $("#dmstc_yn").val();
    	if (dmstcValue) {
	        var dmstcs = dmstcValue.split(',');
	        $("input[type='checkbox'][data-filter='dmstc']").each(function() {
	            var savedDmstcs = $(this).val();
	            if (dmstcs.includes(savedDmstcs)) {
	            	$(this).prop('checked', true);
	        	} else {
	            	$(this).prop('checked', false);
	        	}
	            
	        });
        }
    }


	//통합검색 문서 - 언어
	function langCheckedValues(){
		var checkLang = [];
		$("input[type='checkbox'][data-filter='lang']:checked").each(function() {
        // 체크박스 바로 옆에 있는 라벨에서 년도 값 추출
        var lang = $(this).val();
        checkLang.push(lang);
    	});
    	$('#sply_lang_cd').val(checkLang);
    	env_form();
    	$('#pageNum').val(1);
    	$('#historyForm').submit();
	}
    
    function langCheckedStates() {
        var langValue = $("#sply_lang_cd").val();
    	if (langValue) {
	        var lang = langValue.split(',');
	        $("input[type='checkbox'][data-filter='lang']").each(function() {
	            var savedLang = $(this).val();
	            if (lang.includes(savedLang)) {
	            	$(this).prop('checked', true);
	        	} else {
	            	$(this).prop('checked', false);
	        	}
	            
	        });
        }
    }


	//문서 - 발간연도
	function sendCheckedValues(){
		var checkYears = [];
		$("input[type='checkbox'][data-filter='years']:checked").each(function() {
        // 체크박스 바로 옆에 있는 라벨에서 년도 값 추출
        var year = $(this).val();
        checkYears.push(year);
    	});
    	$('#pblcn_yy').val(checkYears);
    	env_form();
    	$('#pageNum').val(1);
    	$('#historyForm').submit();
	}

    
    function restoreCheckedStates() {
		var yearsValue = $("#pblcn_yy").val();
    	if (yearsValue) {
		    var years = yearsValue.split(',');
	        $("input[type='checkbox'][data-filter='years']").each(function() {
	            var savedYears = $(this).val();
	            if (years.includes(savedYears)) {
	            	$(this).prop('checked', true);
	        	} else {
	            	$(this).prop('checked', false);
	        	}
	            
	        });
        }
    }


	// 미디어 - 영상분량
	function mediaCheckedValues(){
		var checkMedia = [];
		$("input[type='checkbox'][data-filter='media']:checked").each(function() {
        var media = $(this).val();
        checkMedia.push(media);
    	});
    	$('#vod_qunty_mi').val(checkMedia);
    	env_form();
    	$('#pageNum').val(1);
    	$('#historyForm').submit();
	}
	
    
    function mediaCheckedStates() {
		var mediaValue = $("#vod_qunty_mi").val();
    	if (mediaValue) {
			var media = mediaValue.split(',');
	        $("input[type='checkbox'][data-filter='media']").each(function() {
	            var savedMedia = $(this).val();
	            if (media.includes(savedMedia)) {
	            	$(this).prop('checked', true);
	        	} else {
	            	$(this).prop('checked', false);
	        	}
	            
	        });
        }
    }
    
    
    // ESG 주제분류
	function sbjtCheckedValues(){
		var checkSbjt = [];
		$("input[type='checkbox'][data-filter='sbjt']:checked").each(function() {
        var sbjt = $(this).val();
        checkSbjt.push(sbjt);
    	});
    	$('#sbjt_gb_nm').val(checkSbjt);
    	env_form();
    	$('#pageNum').val(1);
    	$('#historyForm').submit();
	}

    
    function sbjtCheckedStates() {
		var sbjtValue = $("#sbjt_gb_nm").val();
    	if (sbjtValue) {
			var sbjt = sbjtValue.split(',');
	        $("input[type='checkbox'][data-filter='sbjt']").each(function() {
	            var savedSbjt = $(this).val();
	            if (sbjt.includes(savedSbjt)) {
	            	$(this).prop('checked', true);
	        	} else {
	            	$(this).prop('checked', false);
	        	}
	            
	        });
        }
    }
    
    
    
    // ESG 업무분류
	function taskCheckedValues(){
		var checkTask = [];
		$("input[type='checkbox'][data-filter='task']:checked").each(function() {
        var task = $(this).val();
        checkTask.push(task);
    	});
    	$('#task_clsf_nm').val(checkTask);
    	env_form();
    	$('#pageNum').val(1);
    	$('#historyForm').submit();
	}
	
    function taskCheckedStates() {
		var taskValue = $("#task_clsf_nm").val();
    	if (taskValue) {
			var task = taskValue.split(',');
	        $("input[type='checkbox'][data-filter='task']").each(function() {
	            var savedTask = $(this).val();
	            if (task.includes(savedTask)) {
	            	$(this).prop('checked', true);
	        	} else {
	            	$(this).prop('checked', false);
	        	}
	            
	        });
        }
    }
    



// 기본 실행
$(function() {	
	
	
	/* 필드별 검색 */
	var fieldValue = $('#fields').val(); 
	
	//통합검색
	if (window.location.pathname === "/search/main") {
		if(fieldValue === ""){
			fieldValue = "all";
		}
	    var matchingA = $("#"+fieldValue);
		matchingA.addClass('checked');
		
		if (matchingA.length > 0) {
		    var buttonText = matchingA.text();
		    $('#select_b').text(buttonText);
		} else {
			$(".select-list ul li").find('#all').addClass('checked');
		    $('#select_b').text('통합검색');
		}
		
		$(".select-list ul li").click(function(){
			var choice = $(this).find('a');
			
			$(".select-list ul li").find('a').removeClass('checked');
			
			choice.eq(0).addClass('checked');
			
			var buttonText = choice.eq(0).text();
	        $('#select_b').text(buttonText);
	        
	        var idValue = $(this).find('a').attr('id');
	       	$('#fields').val(idValue);
		});
	}
	
	
	//ESG 자료실
	if (window.location.pathname === "/trend/bbsList") {
		if(fieldValue === ""){
			$("#"+"title").addClass('current');
		}else{
			$("#"+fieldValue).addClass('current');
		}
		
		//정렬값 셋팅 (조회수 순)
		//$('#sort').val('c');
	}
	
	
	//인사이트
	if (window.location.pathname === "/trend/insightsList") {
		if(fieldValue === ""){
			$("#"+"title").addClass('current');
		}else{
			$("#"+fieldValue).addClass('current');
		}
	}
	
	
	//ESG 뉴스
	if(window.location.pathname === "/trend/esgNewsList") {
		if(fieldValue === ""){
			fieldValue = "all";
		}
		
		var matchingA = $("#"+fieldValue);
		matchingA.addClass('checked');
		
		if (matchingA.length > 0) {
		    var buttonText = matchingA.text();
		    $('#select_b').text(buttonText);
		} else {
			$(".select-list ul li").find('#all').addClass('checked');
		    $('#select_b').text('통합검색');
		}
		
		$(".select-list ul li").click(function(){
			var choice = $(this).find('a');
			
			$(".select-list ul li").find('a').removeClass('checked');
			
			choice.eq(0).addClass('checked');
			
			var buttonText = choice.eq(0).text();
	        $('#select_b').text(buttonText);
	        
	        var idValue = $(this).find('a').attr('id');
	       	$('#fields').val(idValue);
		});
		
	}



	/* 통합검색에서 필터 안되는 것을 기본 실행에서 다음과 같이 설정 */
	//통합검색 문서 - 자료 출처
	$("input[type='checkbox'][data-filter='dmstc']").change(function() {
		dmstcCheckedValues();
	});
	dmstcCheckedStates();
	
	
	//통합검색 문서 - 언어
	$("input[type='checkbox'][data-filter='lang']").change(function() {
		langCheckedValues();
	});
	langCheckedStates();
	
	
	//문서 - 발간연도
	$("input[type='checkbox'][data-filter='years']").change(function() {
		sendCheckedValues();
	});
	restoreCheckedStates();
	
	
	// 미디어 - 영상분량
	$("input[type='checkbox'][data-filter='media']").change(function() {
		mediaCheckedValues();
	});
	mediaCheckedStates();






	/* 엔터키 이벤트 */
	$("#search_kwd").on("keydown", function(key) {
		if (key.keyCode == 13) {
			if (window.location.pathname === "/search/main") { //현재 페이지 URL을 기반으로 판단하여 통합검색 페이지 일 때, category를 total로 셋팅
				$('#category').val('TOTAL');
				$('#sort').val('pd');
			}
			if (window.location.pathname === "/trend/bbsList") { 
				$('#sort').val('r');
			}
			if (window.location.pathname === "/trend/insightsList") { 
				$('#sort').val('r');
			}
			if (window.location.pathname === "/trend/esgNewsList") { 
				$('#sort').val('r');
			}
			
			var emptyKwd = $('#search_kwd').val();
			if(emptyKwd == null || emptyKwd == ""){
				$('#sort').val('pd');
			}
			
			submitkwd();
		}
	});
	

	
	/* 뉴스 날짜 기간 선택 */
	var vdate = $("#date").val();
	
	if(vdate == ""||vdate ==null){
		vdate = 'a';
	}
	
	$("#dateSelect").val(vdate);

	//option 값 선택해서 뿌리기
	$('#dateSelect').on('change', function() {
		var selectDate = $('#dateSelect').val();

		$("#date").val(selectDate);
		
		//현재 날짜 가져오기
		var currentDate = new Date();
		
		if(selectDate == "a"){
			//모든 날짜
			$('#startDate').val('');
			$('#endDate').val('');
			$('#historyForm').submit();
		}
		if(selectDate == "d"){
			//최근 1일
			var oneDayAgo = new Date(currentDate);
        	oneDayAgo.setDate(currentDate.getDate() - 1);
        	setStartDateAndEndDate(oneDayAgo, currentDate);
        	$('#historyForm').submit();
		}
		if(selectDate == "w"){
			//최근 1주일
			var oneWeekAgo = new Date(currentDate);
       	 	oneWeekAgo.setDate(currentDate.getDate() - 7);
        	setStartDateAndEndDate(oneWeekAgo, currentDate);
        	$('#historyForm').submit();
		}
		if(selectDate == "m"){
			//최근 1개월
			var oneMonthAgo = new Date(currentDate);
        	oneMonthAgo.setMonth(currentDate.getMonth() - 1);
        	setStartDateAndEndDate(oneMonthAgo, currentDate);
        	$('#historyForm').submit();
		}
		if(selectDate == "6m"){
			//최근 6개월
			var sixMonthsAgo = new Date(currentDate);
        	sixMonthsAgo.setMonth(currentDate.getMonth() - 6);
        	setStartDateAndEndDate(sixMonthsAgo, currentDate);
        	$('#historyForm').submit();
		}
		if(selectDate == "pick"){
			//직접 설정
			$('#pickDate').css('display', 'block');

			$(".datepicker").on("change", function () {
				//선택한 날짜
				var selectedDate = $(this).datepicker("getDate");
				
				setStartDateAndEndDate(selectedDate, selectedDate);
				$('#historyForm').submit();
			});
			
		}else {
			$('#pickDate').css('display', 'none');
		}

		
	});


	// 시작 날짜와 종료 날짜를 설정하는 함수
	function setStartDateAndEndDate(startDate, endDate) {
		// 날짜 형식을 "YYYYMMDD"로 변환
		var formattedStartDate = formatDate(startDate);
		var formattedEndDate = formatDate(endDate);
		
		// 시작 날짜와 종료 날짜 설정
		$('#startDate').val(formattedStartDate);
		$('#endDate').val(formattedEndDate);
	}

	// formatDate 함수 정의
	function formatDate(date) {
	    var year = date.getFullYear();
	    var month = String(date.getMonth() + 1).padStart(2, '0');
	    var day = String(date.getDate()).padStart(2, '0');
	    return year + month + day;
	}
          


	/* 삼일인사이트, esg자료실, esg뉴스에서 검색 시 정확도 순 노출 */
	if($('#kwd').val()) {
		document.getElementById("sort_rLabel").style.display = "block";
	}	
	


	
	/* 카테고리 언더바 활성화 */
	var cate = $('#category').val();
	cate = '#'+cate;
	$(cate).addClass('current');
	
	
	/* 최신순 등 정렬 활성화 */
	var sort = $('#sort').val();
	sort = '#sort_' + sort;
	$(sort).addClass('current');
	
	
	/* esgNewsList 기간 활성화 */
	var underDate = $('#date').val();
	underDate = '#'+underDate;
	$(underDate).addClass('current');




	/* 검색 폼 제출 시 */
	$('#historyForm').submit(function(e) {

		//기본으로 넘겨 줄 값
		$('#categorize').val($('#category').val() != 'TOTAL');
		$('#page').val(1);
		
		
	});


		


    return;
	
});
