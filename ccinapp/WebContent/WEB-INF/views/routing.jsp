<!DOCTYPE html>
<html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>

  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Report</title>
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css"> 
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css"> 
  <style>
    label, input { display:block; }
    input.text { margin-bottom:12px; width:95%; padding: .4em; }
    fieldset { padding:0; border:0; margin-top:25px; }
    h1 { font-size: 1.2em; margin: .6em 0; }
    div#users-contain { width: 350px; margin: 20px 0; }
    div#users-contain table { margin: 1em 0; border-collapse: collapse; width: 100%; }
    div#users-contain table td, div#users-contain table th { border: 1px solid #eee; padding: .6em 10px; text-align: left; }
    .ui-dialog .ui-state-error { padding: .3em; }
    .validateTips { border: 1px solid transparent; padding: 0.3em; }
  </style>
  
  <script src="${pageContext.request.contextPath}/resources/js/jquery-1.12.4.js"></script>
  <script src="${pageContext.request.contextPath}/resources/js/jquery-ui.js"></script>

  <script>
  $( function() {
    var dialog, form,
      operatorName = $( "#operatorName" ),
      networkName = $( "#networkName" ),
      noOfConversion = $( "#noOfConversion" ),
      allFields = $( [] ).add( operatorName ).add( networkName ).add( noOfConversion ),
      tips = $( ".validateTips" ); 
    
  
    
    function submitRouting() {
      	
      	//var id = $(this).attr('id'); 
      	var id=document.getElementById("id").value;
      	var campaignid=document.getElementById("campaignid").value;
      	var serviceid=document.getElementById("serviceid").value;
      	var trafficpercentage=document.getElementById("trafficpercentage").value;
      	alert("campaignid::: "+campaignid+" serviceid:: "+serviceid+" trafficpercentage:: "+trafficpercentage);
      	var url='${pageContext.request.contextPath}/cnt/inapp/update/trafficrouting?campaignid='+campaignid+
      			"&serviceid="+serviceid+"&trafficpercentage="+trafficpercentage+"&trafiicroutingid="+id;
      			$.ajax({
      		        url: url,	     
      		        type: 'GET',    
      		        async: false,
      		        data: {
      		            format: 'jsonp'    	            
      		        },
      		        success: function(response) {    	        	
      		        	var json = $.parseJSON(response);	        	
      		      	dialog.dialog( "close" );
      		        },
      		    error: function(response) {
      		        alert("Some technical issue please try after some time ");
      		    }
      		    });
      
      };
 
    dialog = $( "#dialog-form" ).dialog({
      autoOpen: false,
      height: 600,
      width: 350,
      modal: true,
      buttons: {
        "Manage Routing": submitRouting,
        Cancel: function() {
          dialog.dialog( "close" );
        }
      },
        close: function() {
        form[ 0 ].reset();
        allFields.removeClass( "ui-state-error" );
      }
    });
 
    form = dialog.find( "form" ).on( "submit", function( event ) {
      event.preventDefault();
      alert("dialog.find "+$(this).attr('action'));
      
    });
    
 $("button[name^='edit']").button().on( "click", function() {
    	//alert("edit::::::::::::::: ");
    	var id = $(this).attr('id'); 
     	var url='${pageContext.request.contextPath}/cnt/inapp/find/trafficrouting?id='+id;
     	$.ajax({
	        url: url,	     
	        type: 'GET',    
	        async: false,
	        data: {
	            format: 'jsonp'    	            
	        },
	        success: function(response) {    	        	
	        	var json = $.parseJSON(response);	   
	        	document.getElementById("id").value = json.trafiicRoutingId;
	        	document.getElementById("campaignid").value = json.campaignId;
	        	document.getElementById("serviceid").value = json.serviceId;
	        	document.getElementById("trafficpercentage").value = json.percentageOfTraffic;
	        },
	    error: function(response) {
	        alert("Some technical issue please try after some time ");
	    }
	    });   
        dialog.dialog( "open" );
    });
 
 $("button[name^='add']").button().on( "click", function() {
 	//alert("edit::::::::::::::: ");
 	var id = $(this).attr('id'); 
  	var url='${pageContext.request.contextPath}/cnt/inapp/find/trafficrouting?id=0';
    document.getElementById("id").value = 0;
	document.getElementById("campaignid").value = 0;
	document.getElementById("serviceid").value = 0;
	document.getElementById("trafficpercentage").value = 0;  
     dialog.dialog( "open" );
 });
 
  });
  
  function deleteRouting(id) {
    	
    	var url='${pageContext.request.contextPath}/cnt/inapp/remove/trafficrouting?trafiicroutingid='+id;
    			$.ajax({
    		        url: url,	     
    		        type: 'GET',    
    		        async: false,
    		        data: {
    		            format: 'jsonp'    	            
    		        },
    		        success: function(response) {    	        	
    		        	var json = $.parseJSON(response);	        	
    		      	dialog.dialog( "close" );
    		        },
    		    error: function(response) {
    		        alert("Some technical issue please try after some time ");
    		    }
    		    });
    
    }
 
  </script>
</head>
<body>
  <center>
	<a href="aggstats" >Report</a>
	|| 
	<a href="reload" >Reload Config(${lastreloadtime})</a>
	|| 
	<a href="routing" >Routing </a>
	
 </center>

<table width="80%" border="1" align="center">
	<tr>
		<th>Campaign Id</th>
		<th>Campaign Name</th>
		<th>Add Routing</th>
		<th>Adnetwork Name Operator Name</th>
		
		<th>Advertiser Name</th>
		<th>Advertiser Operator Name</th>
		<th>Service Name</th>
		<th>Percentage Traffic</th>
		<th>Edit</th>
	</tr>

	<c:forEach var="entry" items="${mapcampaignIdIdToVWTrafficRouting}" varStatus="outerloop">
		<c:if test="${not empty entry.value}">
			<c:forEach var="vwTrafficRouting" items="${entry.value}" varStatus="loop">
				<tr bgcolor="">
					<c:if test="${loop.index==0}">
					<td  rowspan="${entry.value.size()}">${vwTrafficRouting.campaignId}					
						</td>
					<td  rowspan="${entry.value.size()}">${vwTrafficRouting.campaignName}
					<a href="#" onclick=""></a>
						</td>
						<td rowspan="${entry.value.size()}">
						<button id="${vwTrafficRouting.trafiicRoutingId}" name="add0"
					 >
					Add</button>
						</td>
						<td  rowspan="${entry.value.size()}">${vwTrafficRouting.adnetworkName}
						 - ${vwTrafficRouting.campaignOperatorName}
						</td>
					</c:if>
					<td>${vwTrafficRouting.advertiserName}</td>
					<td>${vwTrafficRouting.advertiserOperatorName}</td>
					<td>${vwTrafficRouting.serviceName}</td>
					<td>${vwTrafficRouting.percentageOfTraffic}</td>
					
					
					<td>
				
					<button id="${vwTrafficRouting.trafiicRoutingId}" name="edit${loop.index}"
					 >
					Edit</button>
					<a href="" onclick="deleteRouting('${vwTrafficRouting.trafiicRoutingId}')">Delete</a>
					</td>
					
				</tr>
			</c:forEach>
			
		
		</c:if>
	</c:forEach>

</table>

 <div id="dialog-form" title="Manage Routing">
  <p class="validateTips">All form fields are required.</p>
 
  <form id="crform" name="crform" >
    <fieldset>
      <label for="operatorName">Camapaign Name</label><br>
       <input type="hidden" name="id" id="id" value="" class="text ui-widget-content ui-corner-all">
     <select name="campaignid" id="campaignid" class="text ui-widget-content ui-corner-all">
        <option value="">Select Campaign Name</option>
        <c:forEach var="entry" items="${mapCamapignIdToVWCampaignDetail}" varStatus="outerloop">
         <option value="${entry.key}">${entry.key}-${entry.value.campaignName}</option>
       </c:forEach>
      </select>
      
      <label for="servicename">Service Name</label><br>
       <select name="serviceid" id="serviceid" class="text ui-widget-content ui-corner-all">
        <option value="">Select Service Name</option>
        <c:forEach var="entry" items="${mapServiceIdToService}" varStatus="outerloop">
         <option value="${entry.key}">${entry.value.serviceName}</option>
       </c:forEach>
      </select>
      <br>
      <label for="trafficpercentage">Traffic Percentage</label>
      <input type="text" name="trafficpercentage" id="trafficpercentage"
       value="" class="text ui-widget-content ui-corner-all"> 
      
     
    </fieldset>
  </form>
</div>

</body>
</html>
