CREATE TABLE IF NOT EXISTS invoices (
  id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
  pdf_url TEXT,
  user_id TEXT,
  amount INT
);
