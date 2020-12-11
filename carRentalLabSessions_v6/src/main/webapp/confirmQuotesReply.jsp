<%@page import="ds.gae.view.JSPSite"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<% session.setAttribute("currentPage", JSPSite.CONFIRM_QUOTES_RESPONSE); %>
<% String renter = (String) session.getAttribute("renter"); %>
<% String email = (String) session.getAttribute("email"); %>
<% String resid = (String) session.getAttribute("resid"); %>


<%@include file="_header.jsp"%>

<div class="frameDiv" style="margin: 150px 150px;">
    <h2>Thank You</h2>
    <div class="group">
        <p>
        	Dear <%=renter%>, 
            Thanks for using TEYO Car Rental Application Service. 
            Booking status will be sent to your email address "<%=email%>" shortly.
            Please use this Reservation ID <%=renter%> for your reference. 
            
            Thanks, 
            TEYO Team.  
        </p>
    </div>
</div>

<%@include file="_footer.jsp"%>
