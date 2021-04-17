<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<br><br>
<center>
	<p>
		<a href="aggstats" >Config</a>
	
	<p>
	</center>
	<table id="reporttable" width="80%" border="1" align="center">

	     
					
	<tr>
		<th>Campaign ID</th>
		<th>Advertiser Name</th>
		<th>Service Name</th>
		<th>Create Date</th>
		<th>Unique Pin Request Count</th>
		<th>Unique Pin Send Count</th>
		<th>Unique Pin Validation Count</th>
		
	</tr>
			
				<tr bgcolor="">
					<td>${uniqueCount.cmpId}</td>
					<td>${uniqueCount.advertiserName}</td>
					<td>${uniqueCount.serviceName}</td>
					<td>${uniqueCount.createtime}</td>	
					<td>${uniqueCount.uniquePinRequestCount}</td>	
					<td>${uniqueCount.uniquePinSendCount}</td>						
					<td>${uniqueCount.uniquePinValidateCount}</td>	
					
				</tr>
	
			</table>
</body>
</html>