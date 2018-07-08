$(function() {
    var url = "/bin/osgidemo/get/weather?cityName=" + $('.js-cityname').val();
	 $.ajax({url: url, success: function(result){
        $('.js-temp-show').html(result.temperature);
    }});
});
