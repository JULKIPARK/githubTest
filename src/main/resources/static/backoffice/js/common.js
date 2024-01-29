function datePicker(){
    if($(".datepicker").length){
        $(".datepicker").datepicker({
             ignoreReadonly:true ,
             dateFormat: 'yy.mm.dd',
             monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
             dayNamesMin: ['일','월','화','수','목','금','토'],
             changeMonth: true, //월변경가능
             changeYear: true, //년변경가능
             showMonthAfterYear: true //년 뒤에 월 표시
         });
   }
}
function fileupload(){
    var fileTarget = $('.fileUpload');
     fileTarget.find(".upload").on('change', function(){
        if(window.FileReader){
            var filename = $(this)[0].files[0].name;
        }
            else {
                filename = $(this).val().split('/').pop().split('\\').pop();
        }
        $(this).parents(".fileUpload").find(".url").val(filename);
    });
}
function layerClose(){
    $(".modal-wrap .btn-close").on('click',function(){
        // $(this).parents(".modal-wrap").find(".dimmed").hide();
        $(this).parents(".modal-wrap").removeClass("show");
    })
}
function tab(){
    $(".tab-wrap .tab-list li a").on('click',function(){
        var num = $(this).closest("li").index();
        if($(this).hasClass('current')){
            $(".tab-list li a").removeClass("current");
            return false;
        }else{
            $(".tab-wrap .tab-list li a").removeClass("current");
            $(this).addClass("current");
            $(".tab-wrap .cont-wrap .cont").hide();
            $(".tab-wrap .cont-wrap .cont").eq(num).show();
        }

    })   
}
$(function () {
    datePicker();
    fileupload();
    layerClose();
    tab();
})