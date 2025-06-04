import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Registration from './pages/Registration';
import Login from './pages/Login';
// import Home from './pages/Home'; // You'll likely have a Home page or other pages

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Define your routes here */}
        <Route path="/register" element={<Registration />} />
        <Route path="/login" element={<Login />} />
        <Route path="/" element={<Login />} /> {/* Home page, or default route */}
        {/* Add more routes for other pages as needed */}
      </Routes>
    </BrowserRouter>
  );
}

export default App;