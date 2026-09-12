import React from 'react';
import { Result, Button } from 'antd';
import { useNavigate } from 'react-router-dom';

const ComingSoon = ({ title = 'Tính năng đang phát triển' }) => {
  const navigate = useNavigate();
  return (
    <Result
      status="info"
      title={title}
      subTitle="Giao diện này đang được xây dựng và sẽ sớm ra mắt trong bản cập nhật tới."
      extra={<Button type="primary" onClick={() => navigate('/admin')}>Về Dashboard</Button>}
    />
  );
};

export default ComingSoon;
