import { useState, useEffect } from 'react';
import api from '../api/axios';

function MyRentals() {
  const [rentals, setRentals] = useState([]);
  const [error, setError] = useState('');

  const loadRentals = () => {
    api.get('/rentals/my-rentals')
      .then((res) => setRentals(res.data))
      .catch(() => setError('Failed to load rentals'));
  };

  useEffect(() => {
    loadRentals();
  }, []);

  const handlePay = async (rentalId) => {
    try {
      const res = await api.post(`/rentals/${rentalId}/pay`);
      const { razorpayOrderId, razorpayKeyId, amount } = res.data;

      const options = {
        key: razorpayKeyId,
        amount: Math.round(amount * 100),
        currency: 'INR',
        name: 'QuickLease',
        description: 'Rental Payment',
        order_id: razorpayOrderId,
        handler: async function (response) {
          try {
            await api.post('/rentals/verify-payment', {
              razorpayOrderId: response.razorpay_order_id,
              razorpayPaymentId: response.razorpay_payment_id,
              razorpaySignature: response.razorpay_signature,
            });
            alert('Payment successful!');
            loadRentals();
          } catch (err) {
            alert('Payment verification failed');
          }
        },
        theme: { color: '#6366f1' },
      };

      const rzp = new window.Razorpay(options);
      rzp.open();
    } catch (err) {
      setError(err.response?.data || 'Failed to initiate payment');
    }
  };

  return (
    <div className="page">
      <h2>My Rentals</h2>
      {error && <p className="msg-error">{error}</p>}
      {rentals.length === 0 && <p>No rentals yet.</p>}
      {rentals.map((r) => (
        <div key={r.id} className="list-card">
          <h3>{r.itemTitle}</h3>
          <p>{r.startDate} → {r.endDate}</p>
          <p className="price">₹{r.totalPrice}</p>
          <span className={`badge ${r.status}`}>{r.status}</span>
          {r.status === 'ACCEPTED' && (
            <div style={{ marginTop: '0.8rem' }}>
              <button onClick={() => handlePay(r.id)}>Pay Now</button>
            </div>
          )}
        </div>
      ))}
    </div>
  );
}

export default MyRentals;