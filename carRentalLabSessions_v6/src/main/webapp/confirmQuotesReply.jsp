<%@page import="ds.gae.view.JSPSite"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<% session.setAttribute("currentPage", JSPSite.CONFIRM_QUOTES_RESPONSE); %>
<% String renter = (String) session.getAttribute("renter"); %>
<% String resid = (String) session.getAttribute("resid"); %>


<%@include file="_header.jsp"%>

<div class="frameDiv" style="margin: 150px 150px;">
    <h2>Thank You</h2>
    <div class="group">
        <p>
        	Dear customer, 
           <p>
            Thanks for using our Car Rental Application Service. <br>
            Booking status will be sent to your email address <i>"<%=renter%>"</i> shortly.<br>
            Please use this Reservation ID <%=resid%> for your reference. 
            </p>
            Thanks, 
            Team.  
        </p>
    </div>
</div>

<%@include file="_footer.jsp"%>
