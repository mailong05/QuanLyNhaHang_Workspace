window.addEventListener('error', function(e) {
  var d = document.createElement('div');
  d.style.cssText = 'color:red;font-size:20px;z-index:9999;position:fixed;top:0;background:white;padding:20px;width:100%;height:100%;';
  d.innerHTML = '<pre>' + (e.error ? e.error.stack : e.message) + '</pre>';
  document.body.appendChild(d);
});
