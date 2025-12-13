<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>DNC Simulator Admin</title>

<!-- Bootstrap 5 -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
	
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

<style>
body {
	min-height: 100vh;
	overflow: hidden;
}

.sidebar {
	width: 240px;
	min-height: 100vh;
	background-color: #212529;
}

.sidebar a {
	color: #adb5bd;
	text-decoration: none;
}

.sidebar a.active, .sidebar a:hover {
	background-color: #343a40;
	color: #fff;
}

.content {
	overflow-y: auto;
	height: calc(100vh - 56px);
	padding: 1.5rem;
}
</style>
</head>

<body class="d-flex">

	<%@ include file="sidebar.jsp"%>

	<div class="flex-grow-1 d-flex flex-column">
		<%@ include file="navbar.jsp"%>

		<!-- main content -->
		<div class="content bg-light">
			<jsp:include page="${contentPage}" />
		</div>
	</div>

</body>
</html>
