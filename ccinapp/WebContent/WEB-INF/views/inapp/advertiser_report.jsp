<!DOCTYPE html>

<html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet"/>
<link href="${pageContext.request.contextPath}/resources/css/jquery.datetimepicker.css"  
rel="stylesheet"/>

  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Report</title>
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
  <script src="${pageContext.request.contextPath}/resources/js/jquery-1.12.4.js"></script>
  <script src="${pageContext.request.contextPath}/resources/js/jquery-ui.js"></script>
  <script  src="${pageContext.request.contextPath}/resources/js/jquery.datetimepicker.js"></script>
<!-- Export excel file -->  
<link href="${pageContext.request.contextPath}/resources/css/tableexport.css" rel="stylesheet">

<script src="${pageContext.request.contextPath}/resources/js/FileSaver.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/tableexport.js"></script>
<!-- End Export excel file --> 
  <style>
  	 .mtable  { 
     width:40%; 
    
     }
     .mtable  tr td { 
     width:25%; 
     text-align:center;
     align=center;
     padding: 3px;
    
     }
    table{
    border-spacing: 0px;
    border-collapse: separate;} 
    select {
    
    padding:3px;
    margin: 0;
    -webkit-border-radius:4px;
    -moz-border-radius:4px;
    border-radius:4px;
    -webkit-box-shadow: 0 3px 0 #ccc, 0 -1px #fff inset;
    -moz-box-shadow: 0 3px 0 #ccc, 0 -1px #fff inset;
    box-shadow: 0 3px 0 #ccc, 0 -1px #fff inset;
    background: #f8f8f8;
    color:#888;
    border:none;
    outline:none;
    display: inline-block;
    -webkit-appearance:none;
    -moz-appearance:none;
    appearance:none;
    cursor:pointer;
}
</style>
    <script>
    
  $( function() {  
	  //$("table").tableExport();
	  $("#reporttable").tableExport({
  // Displays table headers (th or td elements) in the <thead>
  headers: true,                    
  // Displays table footers (th or td elements) in the <tfoot>    
  footers: true, 
  // Filetype(s) for the export
  formats: ["xls", "csv", "txt"],           
  // Filename for the downloaded file
  fileName: "id",                         
  // Style buttons using bootstrap framework  
  bootstrap: false,
  // Automatically generates the built-in export buttons for each of the specified formats   
  exportButtons: true,                          
  // Position of the caption element relative to table
  position: "bottom",                   
  // (Number, Number[]), Row indices to exclude from the exported file(s)
  ignoreRows: null,                             
  // (Number, Number[]), column indices to exclude from the exported file(s)              
  ignoreCols: null,                   
  // Removes all leading/trailing newlines, spaces, and tabs from cell text in the exported file(s)     
  trimWhitespace: false         
});
    $( "#fromTime" ).datepicker({ dateFormat: 'yy-mm-dd'});
    $( "#toTime" ).datepicker({ dateFormat: 'yy-mm-dd'});
  });
 </script>
	
	<script type="text/javascript">
	function callProduct(opid){
	  // alert("change:::::::::: "+opid+", serverip: <%=request.getLocalAddr()%>");
	   
   // var opid = $(this).val();
    $.ajax({
        type: 'GET',
        url: "http://<%=request.getLocalAddr()%>/ccinapp/cnt/inapp/productdetail?opid=" + opid,
        success: function(data){
            var product=$('#productId'), option="";
            product.empty();
            option = option + "<option value='0'>Select Product</option>";
            for(var i=0; i<data.length; i++){
                option = option + "<option value='"+data[i].id + "'>"+data[i].productName + "</option>";
            }
            product.append(option);
        },
        error:function(xhr, ajaxOptions, thrownError){
        	//alert(xhr.status);
            alert("Error");
        }
    });
};
</script>
	

<script type="text/javascript">
$(function(){
$('.formselect').change(function(){
	// alert("change");
	//$("#otpform").attr("action", "${pageContext.request.contextPath}/cnt/bz/change/msisdnprefix");
	$("#reportform").submit();
})});
</script>
</head>
<body>

	<center>
	<p>
		<a href="adnoconfig" >Config</a>
	
	<p>
	</center>
    
<form:form  modelAttribute="AggReport" name="reportform" id="reportform"
 action="${pageContext.request.contextPath}/cnt/inapp/advertreport">
 
 
	
<table  border="1" class="mtable" align="center">

	 <tr>
     
     <td>
         <p>Advertiser
             <form:select class="formselect" name="advertiserid" id="advertiserid" path="advertiserid" > 
						<form:option value="" label="Select Advertiser" />
						<c:forEach var="advertiser" items="${advertiserList}"
								varStatus="advertiserloop">
							<form:option value="${advertiser.id}" 
							label="${advertiser.advertiserName}"></form:option>
						</c:forEach>
				</form:select>
		   </p>
           </td>                     
           <td>
           <p>
             	  <label for="actionType">Action Type :</label>&nbsp;		
    			 <form:select id="actionType" path="actionType">
						<form:option value="SEND_PIN" label="SEND_PIN" />
						<form:option value="PIN_VALIDATION" label="PIN_VALIDATION" />
						<form:option value="STATUS_CHECK" label="STATUS_CHECK" />						
				</form:select>
           </td>  
           </tr>
  <tr>
  <td > 
  	  <label for="fromTime">From Time :</label>&nbsp;		
     <form:input type="text" path="fromTime" name="fromTime" id="fromTime" 
      class="text ui-widget-content ui-corner-all"/>
   </td>
    <td > 
      <label for="toTime">To Time :</label>&nbsp;
      <form:input type="text" path="toTime" name="toTime" id="toTime" 
         class="text ui-widget-content ui-corner-all"/>
   </td>
     </tr>     
               <tr>   
           <td>
           <p>
           Campaign ID
             <input type = "text" name = "cmpid" id ="cmpid">
             	</p>
          
				</p>
           </td>     
       
          </tr>
     
          


      <tr>
      <td colspan="2">
      <input type="submit" value="Find Report" />
   
     
      </td></tr>            
	</table>
	<br>
	<br><br>
	<table id="reporttable" width="80%" border="1" align="center">

		<tr ><td colspan="8">&nbsp;</td><tr>
	     
					
	<tr>
	    <td>Campaign Id</td>
		<th>Advertiser Name</th>
		<th>Service Name</th>
		<th>Create Date</th>
		<th>Advertiser Request</th>
		<th>Advertiser Response</th>
		
	</tr>
			<c:forEach var="liveReport" items="${reportlist}" varStatus="loop">
				<tr bgcolor="">
					<td>${liveReport.cmpId}</td>
					<td>${liveReport.advertiserName}</td>	
					<td>${liveReport.serviceName}</td>	
					<td>${liveReport.createDate}</td>						
					<td>${liveReport.advertiserApiRequest}</td>						
					<td>${liveReport.advertiserApiResponse}</td>
					
				</tr>
			</c:forEach>	
	
			</table>
	</form:form>
	<br><br>
	<br><br>
</body>
</html>