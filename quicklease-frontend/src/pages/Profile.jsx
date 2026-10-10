import { useState, useEffect } from 'react';
import api from '../api/axios';

function Profile() {
  const [form, setForm] = useState({ phone: '', address: '', city: '' });
  const [info, setInfo] = useState(null);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    api.get('/users/me')
      .then((res) => {
        setInfo(res.data);
        setForm({
          phone: res.data.phone || '',
          address: res.data.address || '',
          city: res.data.city || '',
        });
      })
      .catch(() => setError('Failed to load profile'));
  }, []);

  const handleChange = (e) => {
    let value = e.target.value;
    if (e.target.name === 'phone') {
      value = value.replace(/\D/g, '').slice(0, 10);
    }
    setForm({ ...form, [e.target.name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    try {
      await api.put('/users/me', form);
      setSuccess('Profile saved!');
    } catch (err) {
      setError(err.response?.data || 'Failed to save profile');
    }
  };

  if (!info && !error) return <div className="page"><p>Loading...</p></div>;

  return (
    <div className="page">
      <div className="form-box">
        <h2>My Profile</h2>
        {info && (
          <p style={{ color: 'var(--text-dim)', fontSize: '0.88rem' }}>
            {info.fullName} • {info.email}{' '}
            {info.emailVerified ? '✓ verified' : '(email not verified)'}
          </p>
        )}
        <form onSubmit={handleSubmit}>
          <label>Phone</label>
          <input name="phone" inputMode="numeric" placeholder="10-digit mobile number"
                 value={form.phone} onChange={handleChange} />
          <label>City</label>
          <input name="city" placeholder="e.g. Pune" value={form.city} onChange={handleChange} />
          <label>Address (optional)</label>
          <textarea name="address" placeholder="Area, street, landmark"
                    value={form.address} onChange={handleChange} />
          <button type="submit">Save</button>
        </form>
        {error && <p className="msg-error">{String(error)}</p>}
        {success && <p className="msg-success">{success}</p>}
      </div>
    </div>
  );
}

export default Profile;