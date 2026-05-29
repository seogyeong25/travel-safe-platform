/**
 * SAFE TRIP - csrf-fetch.js
 * Spring Security CSRF 토큰을 fetch 요청 헤더에 자동 포함합니다.
 * 페이지 <head>에 fragments/csrf 메타 태그가 있어야 합니다.
 */
(function () {
  /** meta 태그에서 CSRF 토큰·헤더명 읽기 */
  function readCsrf() {
    const token = document.querySelector('meta[name="_csrf"]')?.getAttribute("content");
    const header = document.querySelector('meta[name="_csrf_header"]')?.getAttribute("content");
    if (!token || !header) {
      return null;
    }
    return { token, header };
  }

  /** 전역 fetch 래퍼: 동일 origin API 호출 시 CSRF 헤더 부착 */
  window.csrfFetch = function (url, options) {
    const opts = options || {};
    const headers = new Headers(opts.headers || {});
    const csrf = readCsrf();
    if (csrf) {
      headers.set(csrf.header, csrf.token);
    }
    return fetch(url, Object.assign({}, opts, { headers }));
  };
})();
