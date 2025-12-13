<%@ page contentType="text/html;charset=UTF-8"%>

<div class="sidebar d-flex flex-column p-3">
	<h5 class="text-white mb-4">DNC Admin</h5>

	<ul class="nav nav-pills flex-column gap-1">

		<!-- Dashboard -->
		<li class="nav-item"><a
			class="nav-link ${activeMenu == 'dashboard' ? 'active' : ''}"
			href="${pageContext.request.contextPath}/dashboard"> 📊 Dashboard
		</a></li>

		<!-- Master Data -->
		<li class="nav-item"><a
			class="nav-link d-flex justify-content-between align-items-center
               ${activeMenuGroup == 'master' ? 'active' : ''}"
			data-bs-toggle="collapse" href="#masterMenu"> 🗂 Master Data <span>▾</span>
		</a>

			<div class="collapse ${activeMenuGroup == 'master' ? 'show' : ''}"
				id="masterMenu">
				<ul class="nav flex-column ms-3 mt-1">

					<li><a
						class="nav-link ${activeMenu == 'jobs' ? 'active' : ''}"
						href="${pageContext.request.contextPath}/master/jobs"> Jobs </a></li>

					<li><a
						class="nav-link ${activeMenu == 'items' ? 'active' : ''}"
						href="${pageContext.request.contextPath}/master/items"> Items
					</a></li>

					<li><a
						class="nav-link ${activeMenu == 'stats' ? 'active' : ''}"
						href="${pageContext.request.contextPath}/master/stats"> Stats
					</a></li>

					<li><a
						class="nav-link ${activeMenu == 'rarity' ? 'active' : ''}"
						href="${pageContext.request.contextPath}/master/rarity">
							rarity </a></li>

					<li><a
						class="nav-link ${activeMenu == 'set-bonus' ? 'active' : ''}"
						href="${pageContext.request.contextPath}/master/set-bonus">
							set-bonus </a></li>

				</ul>
			</div></li>

		<!-- Users -->
		<li class="nav-item"><a
			class="nav-link ${activeMenu == 'users' ? 'active' : ''}"
			href="${pageContext.request.contextPath}/users"> 👤 Users </a></li>

	</ul>
</div>
