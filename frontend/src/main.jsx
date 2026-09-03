import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import axios from 'axios'

axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401) {
      sessionStorage.removeItem('accessToken');
      sessionStorage.removeItem('role');
      if (window.location.pathname !== '/login') {
         window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
)
