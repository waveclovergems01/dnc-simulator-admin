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
						class="nav-link ${activeMenu == 'categories' ? 'active' : ''}"
						href="${pageContext.request.contextPath}/master/categories">
							Categories </a></li>

					<li><a
						class="nav-link ${activeMenu == 'items' ? 'active' : ''}"
						href="${pageContext.request.contextPath}/master/items"> Items
							Type </a></li>

					<li><a
						class="nav-link ${activeMenu == 'stats' ? 'active' : ''}"
						href="${pageContext.request.contextPath}/master/stats"> Stats
					</a></li>

					<li><a
						class="nav-link ${activeMenu == 'equipment' ? 'active' : ''}"
						href="${pageContext.request.contextPath}/master/equipment">
							Equipment Item </a></li>

					<li><a
						class="nav-link ${activeMenu == 'rarities' ? 'active' : ''}"
						href="${pageContext.request.contextPath}/master/rarities">
							Rarity </a></li>

					<li><a
						class="nav-link ${activeMenu == 'suffix' ? 'active' : ''}"
						href="${pageContext.request.contextPath}/master/suffix">
							Suffix </a></li>

					<li><a
						class="nav-link ${activeMenu == 'suffix-items' ? 'active' : ''}"
						href="${pageContext.request.contextPath}/master/suffix-items">
							Suffix Items </a></li>

					<li><a
						class="nav-link ${activeMenu == 'set-bonus' ? 'active' : ''}"
						href="${pageContext.request.contextPath}/master/set-bonus">
							Set Bonus </a></li>

				</ul>
			</div></li>
		<li class="nav-item"><a
			class="nav-link d-flex justify-content-between align-items-center
	               ${activeMenuGroup == 'json' ? 'active' : ''}"
			data-bs-toggle="collapse" href="#jsonMenu"> 🗂 JSON Data <span>▾</span>
		</a>

			<div class="collapse ${activeMenuGroup == 'json' ? 'show' : ''}"
				id="jsonMenu">
				<ul class="nav flex-column ms-3 mt-1">

					<li><a
						class="nav-link ${activeMenu == 'export' ? 'active' : ''}"
						href="${pageContext.request.contextPath}/json/export"> Export
					</a></li>
				</ul>
			</div></li>

		<!-- Users -->
		<li class="nav-item"><a
			class="nav-link ${activeMenu == 'users' ? 'active' : ''}"
			href="${pageContext.request.contextPath}/users"> 👤 Users </a></li>

	</ul>
</div>
