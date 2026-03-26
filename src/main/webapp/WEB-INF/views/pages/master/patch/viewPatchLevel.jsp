<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- ================= DataTables CSS ================= -->
<link rel="stylesheet"
      href="https://cdn.datatables.net/1.13.8/css/dataTables.bootstrap5.min.css" />

<div class="d-flex justify-content-between align-items-center mb-3">
    <h3 class="m-0">Patch Level</h3>

    <a class="btn btn-primary"
       href="${pageContext.request.contextPath}/master/patch/addPatchLevel">
        + Add Patch Level
    </a>
</div>

<div class="card">
    <div class="card-body">
        <div class="table-responsive">
            <table id="levelTable" class="table table-striped table-hover align-middle">
                <thead>
                    <tr>
                        <th style="width: 90px;">ID</th>
                        <th>Level</th>
                        <th style="width: 200px;">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="t" items="${levelList}">
                        <tr>
                            <td>${t.id}</td>
                            <td>${t.level}</td>
                            <td>
                                <a class="btn btn-sm btn-warning"
                                   href="${pageContext.request.contextPath}/master/patch/editPatchLevel?id=${t.id}">
                                    Edit
                                </a>

                                <form action="${pageContext.request.contextPath}/master/patch/deletePatchLevel"
                                      method="post"
                                      style="display:inline;"
                                      onsubmit="return confirm('Are you sure you want to delete this level?');">
                                    <input type="hidden" name="id" value="${t.id}" />
                                    <button type="submit" class="btn btn-sm btn-danger">
                                        Delete
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- ================= DataTables JS ================= -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.datatables.net/1.13.8/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.8/js/dataTables.bootstrap5.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/js/master/patch/viewPatchLevel.js"></script>