import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';

const Registration = () => {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [termsAgreed, setTermsAgreed] = useState(false);
  const [usernameError, setUsernameError] = useState('');
  const [emailError, setEmailError] = useState('');
  const [passwordError, setPasswordError] = useState('');
  const [confirmPasswordError, setConfirmPasswordError] = useState('');
  const [termsError, setTermsError] = useState('');
  const [generalError, setGeneralError] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const navigate = useNavigate();

  const validateEmail = (email) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };

  const validatePassword = (password) => {
    return password.length >= 8;
  };

  const handleUsernameChange = (event) => {
    setUsername(event.target.value);
    setUsernameError('');
    if (validateEmail(event.target.value)) {
      setUsernameError('Username should not be an email address.');
    }
  };

  const handleEmailChange = (event) => {
    setEmail(event.target.value);
    setEmailError('');
    if (!validateEmail(event.target.value)) {
      setEmailError('Invalid email format.');
    }
  };

  const handlePasswordChange = (event) => {
    setPassword(event.target.value);
    setPasswordError('');
    if (!validatePassword(event.target.value)) {
      setPasswordError('Password must be at least 8 characters long.');
    }
  };

  const handleConfirmPasswordChange = (event) => {
    setConfirmPassword(event.target.value);
    setConfirmPasswordError('');
    if (password !== event.target.value) {
      setConfirmPasswordError('Passwords do not match.');
    }
  };

  const handleTermsChange = (event) => {
    setTermsAgreed(event.target.checked);
    setTermsError('');
    if (!event.target.checked) {
      setTermsError('You must agree to the Terms of Service and Privacy Policy.');
    }
  };

  const handleFirstNameChange = (event) => {
    setFirstName(event.target.value);
  };

  const handleLastNameChange = (event) => {
    setLastName(event.target.value);
  };

  const handlePhoneNumberChange = (event) => {
    setPhoneNumber(event.target.value);
  };

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setGeneralError('');

    let isValid = true;

    if (!username.trim()) {
      setUsernameError('Username is required.');
      isValid = false;
    } else if (validateEmail(username)) {
      setUsernameError('Username should not be an email address.');
      isValid = false;
    }

    if (!email.trim()) {
      setEmailError('Email is required.');
      isValid = false;
    } else if (!validateEmail(email)) {
      setEmailError('Invalid email format.');
      isValid = false;
    }

    if (!password) {
      setPasswordError('Password is required.');
      isValid = false;
    } else if (!validatePassword(password)) {
      setPasswordError('Password must be at least 8 characters long.');
      isValid = false;
    }

    if (password !== confirmPassword) {
      setConfirmPasswordError('Passwords do not match.');
      isValid = false;
    }

    if (!termsAgreed) {
      setTermsError('You must agree to the Terms of Service and Privacy Policy.');
      isValid = false;
    }

    if (isValid) {
      const userData = {
        username,
        email,
        password,
        firstName,
        lastName,
        phoneNumber,
      };

      try {
        const response = await fetch('http://localhost:8080/api/auth/register', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(userData),
        });

        console.log('Full Response:', response);

        if (response.ok) {
          try {
            const data = await response.json();
            console.log('Registration successful:', data);
            if (data && data.token) {
              sessionStorage.setItem('token', JSON.stringify(data));
              navigate('/dashboard');
            } else {
              setGeneralError('Registration successful, but no token received.');
            }
          } catch (error) {
            console.error('Error parsing JSON:', error);
            setGeneralError('Registration successful, but failed to parse response.');
          }
        } else {
          try {
            const errorData = await response.json();
            setGeneralError(errorData.auth || 'Registration failed.');
            console.error('Registration failed:', errorData);
          } catch (error) {
            const errorMessage = await response.text();
            setGeneralError(errorMessage || 'Registration failed.');
            console.error('Registration failed (non-JSON error):', errorMessage);
          }
        }
      } catch (error) {
        setGeneralError('Error: ' + error.message);
        console.error('Error during registration:', error);
      }
    }
  };

  return (
    <div>
      <h2>Register</h2>
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
          {usernameError && <p style={{ color: 'red' }}>{usernameError}</p>}
        </div>
        <div>
          <label htmlFor="email">Email:</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={handleEmailChange}
            required
          />
          {emailError && <p style={{ color: 'red' }}>{emailError}</p>}
        </div>
        <div>
          <label htmlFor="firstName">First Name:</label>
          <input
            type="text"
            id="firstName"
            value={firstName}
            onChange={handleFirstNameChange}
          />
        </div>
        <div>
          <label htmlFor="lastName">Last Name:</label>
          <input
            type="text"
            id="lastName"
            value={lastName}
            onChange={handleLastNameChange}
          />
        </div>
        <div>
          <label htmlFor="phoneNumber">Phone Number:</label>
          <input
            type="text"
            id="phoneNumber"
            value={phoneNumber}
            onChange={handlePhoneNumberChange}
          />
        </div>
        <div style={{ marginBottom: '10px' }}>
          <label>Show Passwords:</label>
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
          {passwordError && <p style={{ color: 'red' }}>{passwordError}</p>}
        </div>
        <div>
          <label htmlFor="confirmPassword">Confirm Password:</label>
          <input
            type={showPassword ? 'text' : 'password'}
            id="confirmPassword"
            value={confirmPassword}
            onChange={handleConfirmPasswordChange}
            required
          />
          {confirmPasswordError && <p style={{ color: 'red' }}>{confirmPasswordError}</p>}
        </div>
        <div>
          <label>
            <input
              type="checkbox"
              checked={termsAgreed}
              onChange={handleTermsChange}
              required
            />
            I agree to the <a href="/terms">Terms of Service</a> and <a href="/privacy">Privacy Policy</a>.
          </label>
          {termsError && <p style={{ color: 'red' }}>{termsError}</p>}
        </div>
        <button type="submit">Sign Up</button>
      </form>
    </div>
  );
};
export default Registration;