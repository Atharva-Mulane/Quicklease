import { useState } from 'react';
import api from '../api/axios';
import { useNavigate } from 'react-router-dom';

function CreateListing() {
  const [form, setForm] = useState({ title: '', description: '', pricePerDay: '', category: 'TOOLS', securityDeposit: '' });
  const [imageFile, setImageFile] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const res = await api.post('/items', {
        ...form,
        pricePerDay: parseFloat(form.pricePerDay),
        securityDeposit: form.securityDeposit ? parseFloat(form.securityDeposit) : 0,
      });

      const newItemId = res.data.id;

      if (imageFile) {
        const formData = new FormData();
        formData.append('file', imageFile);
        await api.post(`/items/${newItemId}/image`, formData, {
          headers: { 'Content-Type': 'multipart/form-data' },
        });
      }

      navigate('/');
    } catch (err) {
      setError(err.response?.data || 'Failed to create listing');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page">
      <div className="form-box">
        <h2>Create Listing</h2>
        <form onSubmit={handleSubmit}>
          <input name="title" placeholder="Title" onChange={handleChange} />
          <textarea name="description" placeholder="Description" onChange={handleChange} />
          <input name="pricePerDay" type="number" step="0.01" placeholder="Price per day (₹)" onChange={handleChange} />
          <input name="securityDeposit" type="number" step="0.01" placeholder="Security deposit (optional)" onChange={handleChange} />
          <select name="category" onChange={handleChange} value={form.category}>
            <option value="TOOLS">Tools</option>
            <option value="ELECTRONICS">Electronics</option>
            <option value="CAMPING">Camping</option>
            <option value="SPORTS">Sports</option>
            <option value="FURNITURE">Furniture</option>
            <option value="VEHICLES">Vehicles</option>
            <option value="OTHER">Other</option>
          </select>
          <label>Photo (optional)</label>
          <input type="file" accept="image/*" onChange={(e) => setImageFile(e.target.files[0])} />
          <button type="submit" disabled={loading}>{loading ? 'Creating...' : 'Create'}</button>
        </form>
        {error && <p className="msg-error">{JSON.stringify(error)}</p>}
      </div>
    </div>
  );
}

export default CreateListing;