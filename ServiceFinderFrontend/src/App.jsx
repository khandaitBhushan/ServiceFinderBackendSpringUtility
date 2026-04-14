import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Register from './pages/Register';
import UserDashboard from './pages/UserDashboard';
import ProviderDashboard from './pages/ProviderDashboard';

const ProtectedRoute = ({ children, allowedRole }) => {
  const { user, role, loading } = useAuth();
  
  if (loading) return <div>Loading...</div>;
  if (!user) return <Navigate to="/login" />;
  if (allowedRole && role !== allowedRole) {
    return <Navigate to={role === 'PROVIDER' ? '/provider-dashboard' : '/user-dashboard'} />;
  }
  
  return children;
};

const AppRoutes = () => {
  const { user, role } = useAuth();

  return (
    <>
      <Navbar />
      <div className="p-4 w-full">
        <Routes>
          <Route path="/" element={
            !user ? <Navigate to="/login" /> :
            role === 'PROVIDER' ? <Navigate to="/provider-dashboard" /> :
            <Navigate to="/user-dashboard" />
          } />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route 
            path="/user-dashboard" 
            element={<ProtectedRoute allowedRole="USER"><UserDashboard /></ProtectedRoute>} 
          />
          <Route 
            path="/provider-dashboard" 
            element={<ProtectedRoute allowedRole="PROVIDER"><ProviderDashboard /></ProtectedRoute>} 
          />
        </Routes>
      </div>
    </>
  );
};

function App() {
  return (
    <AuthProvider>
      <Router>
        <AppRoutes />
      </Router>
    </AuthProvider>
  );
}

export default App;
