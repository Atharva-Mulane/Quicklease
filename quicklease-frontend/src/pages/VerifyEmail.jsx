import { useEffect, useState, useRef } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import api from '../api/axios';

function VerifyEmail() {
  const [searchParams] = useSearchParams();
  const [status, setStatus] = useState('verifying');
  const hasRun = useRef(false);

  useEffect(() => {
    if (hasRun.current) return;
    hasRun.current = true;

    const token = searchParams.get('token');
    if (!token) {
      setStatus('error');
      return;
    }
    api.get(`/auth/verify-email?token=${token}`)
      .then(() => setStatus('success'))
      .catch(() => setStatus('error'));
  }, [searchParams]);

  return (
    <div className="page">
      <div className="form-box">
        {status === 'verifying' && <p>Verifying...</p>}
        {status === 'success' && (
          <>
            <h2>Email Verified!</h2>
            <p style={{ color: 'var(--text-dim)' }}>You can now log in.</p>
            <Link to="/login"><button style={{ marginTop: '1rem' }}>Go to Login</button></Link>
          </>
        )}
        {status === 'error' && (
          <>
            <h2>Verification Failed</h2>
            <p className="msg-error">Invalid or expired link.</p>
          </>
        )}
      </div>
    </div>
  );
}

export default VerifyEmail;