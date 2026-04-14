import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate, Link } from 'react-router-dom';

const Login = () => {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const user = await login(email, password);
      if (user.role === 'PROVIDER') navigate('/provider-dashboard');
      else navigate('/user-dashboard');
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="auth-container card">
      <h2 className="text-center">Login</h2>
      {error && <div className="text-red mb-4 text-center">{error}</div>}
      <form onSubmit={handleSubmit} className="flex-col">
        <input 
          type="email" 
          placeholder="Email address" 
          required 
          value={email} 
          onChange={(e) => setEmail(e.target.value)}
        />
        <input 
          type="password" 
          placeholder="Password" 
          required 
          value={password} 
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit" className="w-full mt-4">Login</button>
      </form>
      <p className="text-center mt-4">
        Don't have an account? <Link to="/register" className="text-blue">Register</Link>
      </p>
    </div>
  );
};

export default Login;
