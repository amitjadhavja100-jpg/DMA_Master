<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Channel Payout Management</title>

<!-- <link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
 -->
<script
	src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js">
	
</script>

<!-- =========================================================
     JQUERY
========================================================= -->

<script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js">
	
</script>

<!-- =========================================================
     FONT AWESOME
========================================================= -->


<!-- =========================================================
     CSS
========================================================= -->


<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/all.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/bootstrap.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/datatables.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/dmastyles.css">
<!-- EXISTING / INLINE CSS -->


<style>
body {
	height: 100%;
	margin: 0;
	font-size: 16px;
	background-color: #f5f6fa;
	margin: 0;
}

/* HEADER */
.navbar {
	background: linear-gradient(#dd5a29, #a73327);
	height: 90px;
	padding: 0px !important;
	display: flex;
	align-items: center;
	padding: 0 20px;
}

.navbar-brand img {
	margin-top: -25px;
	height: 68px;
	margin-right: 15px;
}

.userinfo {
	float: right;
	color: #ffffff;
}

.navbar-nav .nav-link {
	color: #fff !important;
	font-weight: 500;
	margin-right: 15px;
}

.navbar-nav .nav-link.active {
	border-bottom: 2px solid #fff;
}

.user-info {
	color: #fff;
	text-align: right;
	line-height: 1.2;
	font-style: italic;
	font-family: 'Mulish' !important;
	font-size: 16px;
}

.logout-btn {
	font-family: mulish;
	background-color: maroon;
	color: #ffffff;
	width: 8% !important;
	float: right;
	margin-top: 28px;
	margin-right: 10px;
	/* background: #455F98; */
	color: #fff;
	padding: 14.5px 50px;
	font-size: 14px;
	border: 1px solid;
	border-radius: .25rem;
}

/* added by ban502236--start */
.headerMy {
	position: relative;
}

.projectTitle {
	position: absolute;
	left: 50%;
	/*   top: 18px; -- changed by BAN495699*/
	top: 23px;
	transform: translateX(-50%);
	color: #ffffff;
	/*  font-size: 30px;-- changed by BAN495699*/
	font-size: 25px;
	font-weight: bold;
	white-space: nowrap;
}

/* added by ban502236--end */

/* sidebar and main containt */

/* =========================================================
   RESET
========================================================= */
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

/* =========================================================
   PARENT
========================================================= */
#parent_div {
	display: flex;
}

/* =========================================================
   SIDEBAR
========================================================= */
.leftbarMy {
	padding-left: 0;
	width: 100px;
	min-height: calc(100vh - 90px);
    height: auto;
	background: #a73327;
	/* position: fixed; */
	position: relative;
	left: 0;
	top: 0;
	padding-top: 15px;
	overflow-x: hidden;
	overflow-y: auto;
	transition: all .3s ease;
	z-index: 999;
}

/* =========================================================
   EXPAND SIDEBAR
========================================================= */
.leftbarMy.expanded {
	width: 280px;
}

/* =========================================================
   MAIN CONTENT
========================================================= */
/* #main-content {
	margin-left: 8px;
	width: calc(100% - 8px);
	padding: 20px;
	transition: all .3s ease;
} */

#main-content{
    flex:1;
    padding:15px;
    overflow:auto;
}

/* SHIFT CONTENT */
.leftbarMy.expanded ~ #main-content {
	/* margin-left: 280px; */
	/* width: calc(100% - 280px); */
	
}

/* =========================================================
   REMOVE DEFAULT UL STYLE
========================================================= */
.leftbarMy ul {
	list-style: none;
	padding-left: 15px;
	margin: 0;
}

/* =========================================================
   MAIN MENU BUTTON
========================================================= */
.menu-main {
	width: 100%;
	display: flex;
	align-items: center;
	gap: 14px;
	padding: 12px 16px;
	border: none !important;
	background: transparent !important;
	color: white !important;
	text-align: left;
	border-radius: 6px !important;
	box-shadow: none !important;
	transition: 0.3s;
	cursor: pointer;
}

/* HOVER */
.menu-main:hover {
	background: rgba(255, 255, 255, 0.15) !important;
}

/* =========================================================
   ICON
========================================================= */
/* .leftmenuitems {
	min-width: 22px;
	font-size: 18px;
	text-align: center;
	color: white;
	margin-left: 0px;
}
 */
 
 .leftmenuitems{
    min-width: 22px;
    font-size: 22px;
    text-align: center;
    color: white;
    margin-left: 0;
}
 
/* =========================================================
   MENU TEXT
========================================================= */
.menu-text {
	opacity: 0;
	visibility: hidden;
	white-space: nowrap;
	font-size: 15px;
	font-weight: 600;
	color: white;
	transition: 0.2s ease;
}

/* SHOW TEXT */
.leftbarMy.expanded .menu-text {
	opacity: 1;
	visibility: visible;
	text-decoration: none;
	text-wrap: auto;
}

/* =========================================================
   SUBMENU CONTAINER
========================================================= */
.btn-toggle-nav {
	padding-left: 48px;
	padding-top: 4px;
	padding-bottom: 4px;
	position: relative;
}

/* =========================================================
   SUBMENU LEFT LINE
========================================================= */
.btn-toggle-nav::before {
	content: "";
	position: absolute;
	left: 28px;
	top: 8px;
	bottom: 8px;
	width: 1px;
	background: rgba(255, 255, 255, 0.25);
}

/* =========================================================
   HIDE SUBMENU WHEN COLLAPSED
========================================================= */
.leftbarMy:not(.expanded) .collapse {
	display: none !important;
}

/* =========================================================
   SUBMENU LINK
========================================================= */
.submenu-link {
	display: flex;
	align-items: center;
	gap: 10px;
	padding: 10px 14px 10px 18px;
	color: white;
	text-decoration: none;
	border-radius: 6px;
	transition: 0.3s;
	margin-right: 8px;
}

/* HOVER */
.submenu-link:hover {
	background: rgba(255, 255, 255, 0.15);
	color: white;
	text-decoration: none;
}

/* =========================================================
   ACTIVE SUBMENU
========================================================= */
.submenu-link.dma_active {
	background: rgba(255, 255, 255, 0.18);
	border-left: 4px solid white;
	text
}

/* =========================================================
   REMOVE BOOTSTRAP ARROW
========================================================= */
.btn-toggle::after {
	display: none !important;
}

/* =========================================================
   SCROLLBAR
========================================================= */
.leftbarMy::-webkit-scrollbar {
	width: 5px;
}

.leftbarMy::-webkit-scrollbar-thumb {
	background: #ffffff55;
	border-radius: 10px;
}

/* =========================================================
   PAGE CONTAINER
========================================================= */
#page-container {
	background: white;
	border-radius: 10px;
	padding: 20px;
	min-height: calc(100vh - 120px);
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
}

/* End sidebar css  */
</style>
</head>

<body>

	<div>
		<nav class="navbar-expand-lg navbar-light headerMy"> <!-- SMALL LOGO -->
		<img id="logoFull"
			src="${pageContext.request.contextPath}/images/icici_logo_trans.png"
			class="icici_logo">  <!-- added by ban502236--start -->

		<div class="projectTitle">iChannel Payout Processing System</div>
		<!-- added by ban502236--end  -->

		<button
			onclick="window.location.href='${pageContext.request.contextPath}/logout'"
			class="btn login-form__btn submit w-100"
			style="background-color: maroon; color: #ffffff; width: 8% !important; float: right; margin-top: 28px; margin-right: 10px;">Logout</button>


		<div class="userinfo mr-sm-2 mt-sm-1">
			<p class="mb-sm-1 mr-sm-2">
				Welcome <span id="welcomeUser"></span>
			</p>
			<p class="mb-sm-1 mr-sm-2">
				Last Login: <span id="lastLogin"></span>
			</p>
			<p class="mb-sm-1 mr-sm-2">
				Role :<span id="userRole"></span>
			</p>
		</div>
		</nav>
	</div>

	<div id="parent_div">
		<div class="leftbarMy" id="sidebar">

			<ul class="list-unstyled ps-0">

				<!-- =========================================================
			     MASTER MENU
			========================================================= -->

				<li class="mb-1">

					<button class="btn btn-toggle menu-main" data-bs-toggle="collapse"
						data-bs-target="#master-collapse">

						<i class="fa-solid fa-layer-group leftmenuitems"></i> <span
							class="menu-text"> Master </span>

					</button> <!-- SUBMENU -->

					<div class="collapse show" id="master-collapse">

						<ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">

							<li><a href="#" class="submenu-link"
								onclick="loadPageMaster(this,'maker')" data-role="maker"> <i
									class="fa-solid fa-pen leftmenuitems"></i> <span
									class="menu-text"> Maker </span>

							</a></li>

							<li><a href="#" class="submenu-link"
								onclick="loadPageMaster(this,'checker')" data-role="checker">
									<i class="checkerStyle leftmenuitems"></i> <span
									class="menu-text"> Checker </span>

							</a></li>

							<li><a href="#" class="submenu-link"
								onclick="loadPageMaster(this,'reports')" data-role="reports">
									<i class="fa-solid fa-upload leftmenuitems"></i> <span
									class="menu-text"> File Upload / Download </span>

							</a></li>

						</ul>

					</div>

				</li>

				<!-- =========================================================
			     SLAB MENU
			========================================================= -->

				<li class="mb-1">

					<button class="btn btn-toggle menu-main" data-bs-toggle="collapse"
						data-bs-target="#slab-collapse">

						<i class="fa-solid fa-table-cells leftmenuitems"></i> <span
							class="menu-text"> Slab </span>

					</button>

					<div class="collapse" id="slab-collapse">

						<ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">

							<li><a href="#" class="submenu-link"
								onclick="loadPageMaster(this,'SLABMAKER')" data-role="maker"> <i
									class="fa-solid fa-pen leftmenuitems"></i> <span
									class="menu-text"> Maker </span>

							</a></li>

							<li><a href="#" class="submenu-link"
								onclick="loadPageMaster(this,'SLABCHECKER')" data-role="checker">
									<i class="fa-solid fa-check leftmenuitems"></i> <span
									class="menu-text"> Checker </span>

							</a></li>

						</ul>

					</div>

				</li>

				<!-- =========================================================
			     CONFIG MENU
			========================================================= -->


				<li class="mb-1">

					<button class="btn btn-toggle menu-main" data-bs-toggle="collapse"
						data-bs-target="#config-collapse">

						<i class="fa-solid fa-gear leftmenuitems"></i> <span
							class="menu-text"> Config </span>

					</button>

					<div class="collapse" id="config-collapse">

						<ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">

							<li><a href="#" class="submenu-link"
								onclick="loadPageMaster(this,'CONFIGMAKER')" data-role="maker"> <i
									class="fa-solid fa-pen leftmenuitems"></i> <span
									class="menu-text"> Maker </span>

							</a></li>

							<li><a href="#" class="submenu-link"
								onclick="loadPageMaster(this,'CONFIGCHECKER')" data-role="checker">
									<i class="fa-solid fa-check leftmenuitems"></i> <span
									class="menu-text"> Checker </span>

							</a></li>

						</ul>

					</div>

				</li>

			</ul>

		</div>

		<div id="main-content" style="overflow: hidden;">
			<jsp:include page="/pages/${contentPage}"></jsp:include>
		</div>
	</div>


	<script>


				
		/* =========================================================
		   SIDEBAR EXPAND / COLLAPSE
		========================================================= */

		document.addEventListener('DOMContentLoaded',

		function() {

			var sidebar = document.getElementById('sidebar');

			if (sidebar) {

				/* EXPAND */

				sidebar.addEventListener('mouseenter',

				function() {

					sidebar.classList.add('expanded');
				});

				/* COLLAPSE */

				sidebar.addEventListener('mouseleave',

				function() {

					sidebar.classList.remove('expanded');
				});
			}
		});

		/* =========================================================
		   LOAD PAGE
		========================================================= */

		/*function loadPage(pageName, element){

			

			document.querySelectorAll('.submenu-link')
			.forEach(function(item){

				item.classList.remove('dma_active');
			});


			element.classList.add('dma_active');

			

			$("#page-container").load(pageName);

			

			sessionStorage.setItem(
				'activeMenu',
				pageName
			);
		} */

		/* =========================================================
		   RESTORE ACTIVE MENU
		========================================================= */

		document
				.addEventListener(
						'DOMContentLoaded',

						function() {

							var activePage = sessionStorage
									.getItem('activeMenu');

							if (activePage) {

								document
										.querySelectorAll('.submenu-link')
										.forEach(
												function(item) {

													var onclickAttr = item
															.getAttribute('onclick');

													if (onclickAttr
															&& onclickAttr
																	.includes(activePage)) {

														item.classList
																.add('dma_active');
													}
												});
							}
						});
	</script>

	<script>
		/* function loadPageMaster(element, role) {

			document.querySelectorAll('.leftbarMy li').forEach(function(item) {
				item.classList.remove('dma_active');
			});

			element.classList.add('dma_active');
			sessionStorage.setItem("activeTab", role);
			// 3️⃣ Navigate
			window.location.href = "${pageContext.request.contextPath}/mainPage/load?master="
					+ role;
		} */

		function loadPageMaster(element, role) {

			/* REMOVE ACTIVE */

			document.querySelectorAll('.submenu-link').forEach(function(item) {

				item.classList.remove('dma_active');
			});

			/* ADD ACTIVE */

			if (element) {

				element.classList.add('dma_active');
			}

			/* STORE ACTIVE */

			sessionStorage.setItem('activeTab', role);

			/* REDIRECT TO CONTROLLER */

			window.location.href = "${pageContext.request.contextPath}/mainPage/load?master="
					+ role;
		}

		/* GLOBAL */

		/* window.loadPageMaster =
			loadPageMaster; */
	</script>

	<script>
		window.onload = function() {
			var userName = localStorage.getItem("userName");
			var userId = localStorage.getItem("user_id");
			var lastLogin = localStorage.getItem("lastLoginDate");
			var role = localStorage.getItem("role");

			if (userName && userId) {
				document.getElementById("welcomeUser").innerText = userName
						+ " (" + userId + ")";
			}
			document.getElementById("lastLogin").innerText = lastLogin || "";
			document.getElementById("userRole").innerText = role || "";

			fetch("${pageContext.request.contextPath}/mainPage/setUserSession",
					{
						method : "POST",
						headers : {
							"Content-Type" : "application/json"
						},
						body : JSON.stringify({
							 user : userId 
							/* user : 'BAN49380' */
						})
					});
		};
	</script>

</body>
</html>