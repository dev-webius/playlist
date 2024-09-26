<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header>
    <nav class="navbar navbar-expand-lg justify-content-between">
        <!-- 로고 -->
        <div class="d-flex justify-content-lg-start justify-content-center" style="width: 800px">
            <a class="m-4 m-lg-3" href="/">
                <img src="/res/img/playlist/logo.png" style="width: 180px" />
            </a>
        </div>
        <!-- 검색창 -->
        <div class="d-flex justify-content-center mt-2" style="width: 100%">
            <form action="/search" method="get" class="form-inline" id="search" autocomplete="off">
                <i class="fas fa-search" id="icon" onclick="$('#search').submit()"></i>
                <input type="text" class="form-control px-3" id="query" name="q" placeholder="검색어를 입력하세요." value="${resultData.query}" />
            </form>
        </div>
        <!-- 버튼 네비게이션 -->
        <div class="d-none d-lg-flex justify-content-end align-items-center" style="width: 800px">
            <ul class="navbar-nav" data-role="primary">
                <li class="nav-item mr-2">
                    <a class="nav-link" href="/sign">시작하기</a>
                </li>
                <li class="nav-item mr-2">
                    <a class="nav-link" href="/notice">공지사항</a>
                </li>
            </ul>
            <ul class="navbar-nav" data-role="secondary">
                <li class="nav-item">
                    <a class="nav-link" href="/login">로그인</a>
                </li>
            </ul>
        </div>
        <!-- 반응형 네비게이션 -->
        <div class="d-lg-none" data-role="sidebar">
            <i class="fas fa-bars" onclick="$('#side-view').prop('class', 'd-block')"></i>
            <div class="d-none" id="side-view" data-role="side-view">
                <i class="fas fa-times" onclick="$('#side-view').prop('class', 'd-none')"></i>
                <ul class="table table-hover mt-5 pt-3" data-role="side">
                    <li class="mt-1">
                        <a class="px-4 py-2" href="/sign">시작하기</a>
                    </li>
                    <li class="mt-1">
                        <a class="px-4 py-2" href="/notice">공지사항</a>
                    </li>
                    <li class="mt-1">
                        <a class="px-4 py-2" href="/login">로그인</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</header>