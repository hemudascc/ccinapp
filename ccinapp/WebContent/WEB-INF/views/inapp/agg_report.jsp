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


function getCount(){
	$("#countform").submit();
}	
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
		<a href="advertreport">Advertiser Report</a>
	
	<p>
	</center>
    
<form:form  modelAttribute="AggReport" name="reportform" id="reportform"
 action="${pageContext.request.contextPath}/cnt/inapp/aggstats">
 
 
	
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
           Operator
             <form:select class="formselect" name="opid" id="opid" path="opid" > 
						<form:option value="" label="Select Operator" />
						<c:forEach var="operator" items="${operatorList}"
								varStatus="oploop">
							<form:option value="${operator.operatorId}" 
							label="${operator.operatorName}"></form:option>
						</c:forEach>
				</form:select>
				</p>
           </td>  
           </tr>
               <tr>   
           <td>
           <p>
           	Product
             <form:select  name="productid" id="opid" path="productId" > 
						<form:option value="" label="Select Product" />
						<c:forEach var="product" items="${productList}"
								varStatus="productloop">
							<form:option value="${product.id}" 
							label="${product.productName}"></form:option>
						</c:forEach>
				</form:select>
				</p>
           </td>     
           <td>
            <label for="adnetworkId">Adnetwork :</label>&nbsp;
      <form:select id="adnetworkId" path="adnetworkId">
						<form:option value="0" label="Select Adnetwork" />
						<c:forEach var="adnetwork" items="${adnetworksList}"
							varStatus="adnetworkloop">
							<form:option value="${adnetwork.adNetworkId}" 
							label="${adnetwork.networkName}"></form:option>							
						</c:forEach>
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
  <td > 
  	  <label for="reportType">Report Type :</label>&nbsp;		
     <form:select id="reportType" path="reportType">
						<form:option value="Daily" label="Daily" />
						<form:option value="Monthly" label="Monthly" />						
				</form:select>
   </td>
    <td > 
         <p hidden> Aggregator
         <form:select class="formselect" name="aggregatorId" id="aggregatorId" path="aggregatorId" data-selected=""> 
						<form:option value="" label="Select " />
						<c:forEach var="aggregator" items="${listAggregator}"
								varStatus="aggregatorloop">
							<form:option value="${aggregator.id}" 
							 label="${aggregator.name}" ></form:option>
						</c:forEach>
		   </form:select>
		   </p>
   
   </td>
     </tr> 
     
      <tr>
      <td colspan="2">
      <input type="submit" value="Find Report" />
   
     
      </td></tr>            
	</table>
	
	</form:form>
	<br>
	<center>
	 <label for="lastclickupdatetime">Last click Updated Time :  <b>${lastInappLiveReport.lastUpdateTime }</b></label>&nbsp;    	
    </center>
	<br><br>
	<table id="reporttable" width="80%" border="1" align="center">

	<c:forEach var="entry" items="${reportMap}" varStatus="outerloop">
		<tr ><td colspan="8">&nbsp;</td><tr>
	     
					
	<tr>
	    <td>Campaign Id</td>
		<th>Advertiser Name</th>
		<th>Adnetwork Name</th>
		<th>Product Name</th>
		<th>Service Name</th>
		<th>Operator Name</th>
		<th>Report Date</th>		
		<th>Pin Request Count</th>		
		<th>Pin Send Count</th>
		<th>Pin Validation RequestCount</th>
		<th>Pin Validate Count</th>
		<th>Pin Validate Amount</th>
		<th>Status Check Request Count</th>
		<th>Send Conversion Count</th>
		<th>Send Conversion Amount</th>
		<th>Total Revenue</th>
		<th>Total Revenue(USD)</th>
		<th></th>
		
	</tr>
			<c:forEach var="liveReport" items="${entry.value}" varStatus="loop">
				<tr bgcolor="">
					<td>${liveReport.adnetworkCampaignId}</td>
					<td>${liveReport.advertiserName}</td>	
					<td>${liveReport.networkName}</td>	
					<td>${liveReport.productName}</td>							
					<td>
					${liveReport.serviceName}
<%-- 					<c:if test="${productId!=null&&productId>0}"> --%>
<%-- 					${mapProduct[liveReport.productId].productName} --%>
<%-- 					</c:if> --%>
					</td>						
					<td>${liveReport.operatorName}</td>					
					<td>${liveReport.reportDateStr}</td>								
					<td>${liveReport.pinRequestCount}</td>
					<td>${liveReport.pinSendCount}</td>
					<td>${liveReport.pinValidationRequestCount}</td>
					<td>${liveReport.pinValidateCount}</td>
					<td>${liveReport.pinValidateAmount}</td>
					<td>${liveReport.statusCheckRequestCount}</td>
					<td>${(liveReport.sendConversionCount)}</td>
					<td>${liveReport.sendConversionAmount}</td>
					<td></td><td></td>
					<td><p>
					<form:form  modelAttribute="InappLiveReport" name="countform" id="countform" action="${pageContext.request.contextPath}/cnt/inapp/uniquecount">
 								<input type = "hidden" name = "adnetworkCampaignId" id ="adnetworkCampaignId" value="${liveReport.adnetworkCampaignId}">
 								<input type = "hidden" name = "serviceId" id ="serviceId" value="${liveReport.serviceId}">
 								<input type = "hidden" name = "reportDateStr" id ="reportDateStr" value="${liveReport.reportDateStr}">
 								<input type = "hidden" name = "serviceName" id ="serviceName" value="${liveReport.serviceName}">
 								<input type = "hidden" name = "advertiserName" id ="advertiserName" value="${liveReport.advertiserName}">
 								
 								<input type="submit" value="Unique Count" onclick=getCount();/>
					</form:form>
					</p>
					</td>
				</tr>
			</c:forEach>	
			  <tr bgcolor="">						
					<td colspan="5">Total</td>				
					<td></td><td></td>						
					<td>${entry.value.stream().map(v->v.pinRequestCount).sum()}</td>								
					<td>${entry.value.stream().map(v->v.pinSendCount).sum()}</td>
					<td>${entry.value.stream().map(v->v.pinValidationRequestCount).sum()}</td>
					<td>${entry.value.stream().map(v->v.pinValidateCount).sum()}</td>
					<td>${entry.value.stream().map(v->v.pinValidateAmount).sum()}</td>
					<td>${entry.value.stream().map(v->v.statusCheckRequestCount).sum()}</td>
			    	<td>${entry.value.stream().map(v->v.sendConversionCount).sum()}</td>
					<td>${entry.value.stream().map(v->v.sendConversionAmount).sum()}</td>
				</tr>
			</c:forEach>
			</table>
	<br><br>
	<br><br>
</body>
</html>