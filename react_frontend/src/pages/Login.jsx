import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [generalError, setGeneralError] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [token, setToken] = useState('');
  const navigate = useNavigate(); // Initialize useNavigate

  // Check for existing token on component mount
  useEffect(() => {
    const storedToken = localStorage.getItem('token');
    if (storedToken) {
      setToken(storedToken);
      // Redirect to dashboard or another page if already logged in
      navigate('/dashboard'); // Replace '/dashboard' with your actual route
    }
  }, [navigate]);

  const handleUsernameChange = (event) => {
    setUsername(event.target.value);
  };

  const handlePasswordChange = (event) => {
    setPassword(event.target.value);
  };

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setGeneralError('');

    if (!username.trim() || !password) {
      setGeneralError('Username and password are required.');
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          // We need to get the system id or os id which everyone is the most secure practice.
          // Then we assign that id to the Device-Id header.
          'Content-Type': 'application/json',
          'Device-Id' : '123454'
        },
        body: JSON.stringify({ username, password }),
      });

      if (!response.ok) {
        let errorMessage = 'Login failed.';
        if (response.status === 401) {
          errorMessage = 'Invalid credentials. Please check your username and password.';
        } else {
          try {
            const errorData = await response.json();
            if (errorData && errorData.message) {
              errorMessage = errorData.message;
            }
          } catch (e) {
            // If the response is not JSON, use the raw text
            errorMessage = await response.text() || response.statusText;
          }
        }
        setGeneralError(errorMessage);
        return;
      }

      const data = await response.text();
      console.log('Login successful:', data);
      setToken(data);
      // Removing token from localStorage, it's unsecure to store JWT in localStorage
      // localStorage.setItem('token', data);
      sessionStorage.setItem('token', data);
      // Redirect to dashboard or another page after successful login
      navigate('/dashboard'); // Replace '/dashboard' with your actual route
    } catch (error) {
      setGeneralError('Failed to log in. Please try again.');
      console.error('Error:', error);
    }
  };

  return (
    <div>
      <h2>Login</h2>
      {generalError && <p style={{ color: 'red' }}>{generalError}</p>}
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="username">Username:</label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={handleUsernameChange}
            required
          />
        </div>
        <div style={{ marginBottom: '10px' }}>
          <label>Show Password:</label>
          <button
            type="button"
            onClick={togglePasswordVisibility}
            style={{
              background: 'none',
              border: 'none',
              cursor: 'pointer',
              padding: '0',
              marginLeft: '5px',
              verticalAlign: 'middle',
            }}
          >
            <FontAwesomeIcon icon={showPassword ? faEyeSlash : faEye} />
          </button>
        </div>
        <div>
          <label htmlFor="password">Password:</label>
          <input
            type={showPassword ? 'text' : 'password'}
            id="password"
            value={password}
            onChange={handlePasswordChange}
            required
          />
        </div>
        <div>
          <a href="/terms">Terms of Service</a> and <a href="/privacy">Privacy Policy</a>
        </div>
        <button type="submit">Log In</button>
        {token && (
          <div style={{ marginTop: '20px' }}>
            <p>
              <strong>JWT Token:</strong>
            </p>
            <textarea
              readOnly
              value={token}
              rows={3}
              cols={50}
              style={{ width: '100%', maxWidth: '500px' }}
            />
          </div>
        )}
      </form>
    </div>
  );
};

export default Login;
