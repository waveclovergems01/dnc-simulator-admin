<%@ page contentType="text/html;charset=UTF-8"%>

<nav class="navbar navbar-dark bg-dark px-3">
	<span class="navbar-brand">DNCSimulatorAdmin</span>

	<div class="dropdown">
		<a class="text-white dropdown-toggle" href="#"
			data-bs-toggle="dropdown"> Admin </a>
		<ul class="dropdown-menu dropdown-menu-end">
			<li><a class="dropdown-item"
				href="${pageContext.request.contextPath}/profile">Profile</a></li>
			<li><hr class="dropdown-divider"></li>
			<li><a class="dropdown-item text-danger" href="#">Logout</a></li>
		</ul>
	</div>
</nav>

<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
