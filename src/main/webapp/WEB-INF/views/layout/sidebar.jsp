<%@ page contentType="text/html;charset=UTF-8"%>

<div class="sidebar d-flex flex-column p-3">
	<h5 class="text-white mb-4">DNC Admin</h5>

	<ul class="nav nav-pills flex-column gap-1">
		<li class="nav-item"><a
			class="nav-link ${activeMenu == 'dashboard' ? 'active' : ''}"
			href="${pageContext.request.contextPath}/dashboard"> 📊 Dashboard
		</a></li>
		<li class="nav-item"><a
			class="nav-link ${activeMenu == 'users' ? 'active' : ''}"
			href="${pageContext.request.contextPath}/users"> 👤 Users </a></li>
		<li class="nav-item"><a
			class="nav-link ${activeMenu == 'profile' ? 'active' : ''}"
			href="${pageContext.request.contextPath}/profile"> ⚙ Profile </a></li>
	</ul>
</div>
