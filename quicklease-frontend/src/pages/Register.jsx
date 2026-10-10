import { useState } from 'react';
import api from '../api/axios';
import { useNavigate } from 'react-router-dom';

function Register() {
  const [form, setForm] = useState({ email: '', password: '', fullName: '' });
  const [error, setError] = useState('');
  const [registered, setRegistered] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await api.post('/auth/register', form);
      setRegistered(true);
    } catch (err) {
      setError(err.response?.data || 'Registration failed');
    }
  };

  if (registered) {
    return (
      <div className="page">
        <div className="form-box">
          <h2>Check your inbox</h2>
          <p style={{ color: 'var(--text-dim)' }}>
            We've sent a verification link to your email. Please verify before logging in.
          </p>
          <button onClick={() => navigate('/login')} style={{ marginTop: '1rem' }}>Go to Login</button>
        </div>
      </div>
    );
  }

  return (
    <div className="page">
      <div className="form-box">
        <h2>Register</h2>
        <form onSubmit={handleSubmit}>
          <input name="fullName" placeholder="Full Name" onChange={handleChange} />
          <input name="email" type="email" placeholder="Email" onChange={handleChange} />
          <input name="password" type="password" placeholder="Password" onChange={handleChange} />
          <button type="submit">Register</button>
        </form>
        {error && <p className="msg-error">{JSON.stringify(error)}</p>}
      </div>
    </div>
  );
}

export default Register;