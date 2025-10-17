# DietMe Frontend

Next.js 15 frontend for the DietMe diet mentoring platform.

## Tech Stack

- **Framework**: Next.js 15 (App Router)
- **Language**: TypeScript
- **UI Library**: React 18
- **Styling**: Tailwind CSS 3.4
- **API Client**: Native Fetch API

## Getting Started

### Prerequisites

- Node.js 18+ or 20+
- npm, yarn, or pnpm

### Installation

```bash
# Install dependencies
npm install
# or
yarn install
# or
pnpm install
```

### Environment Variables

Copy `.env.example` to `.env.local` and configure:

```bash
cp .env.example .env.local
```

Key variables:
- `NEXT_PUBLIC_API_URL`: Backend API URL (default: http://localhost:8080)

### Development

```bash
# Run development server
npm run dev

# Open http://localhost:3000 in your browser
```

### Build

```bash
# Create production build
npm run build

# Start production server
npm start
```

### Code Quality

```bash
# Run ESLint
npm run lint

# Type check
npm run type-check
```

## Project Structure

```
frontend/
├── src/
│   ├── app/              # Next.js App Router pages
│   │   ├── auth/         # Authentication pages
│   │   ├── dashboard/    # Dashboard pages
│   │   ├── groups/       # Group management pages
│   │   ├── layout.tsx    # Root layout
│   │   ├── page.tsx      # Home page
│   │   └── globals.css   # Global styles
│   ├── components/       # React components
│   ├── lib/              # Utility functions and API client
│   │   └── api-client.ts # Backend API client
│   └── types/            # TypeScript type definitions
│       └── index.ts      # Shared types
├── public/               # Static assets
├── .env.example          # Environment variables template
├── .env.local            # Local environment variables (not committed)
├── next.config.ts        # Next.js configuration
├── tailwind.config.ts    # Tailwind CSS configuration
├── tsconfig.json         # TypeScript configuration
└── package.json          # Dependencies and scripts
```

## API Client Usage

The `api-client.ts` provides a typed fetch wrapper:

```typescript
import { api } from '@/lib/api-client'
import type { User } from '@/types'

// GET request
const users = await api.get<User[]>('/api/users')

// POST request
const newUser = await api.post<User>('/api/users', {
  email: 'user@example.com',
  firstName: 'John',
  lastName: 'Doe',
})

// Error handling
try {
  await api.get('/api/protected')
} catch (error) {
  if (error instanceof ApiError) {
    console.error(`API Error ${error.status}: ${error.message}`)
  }
}
```

## Integration with Backend

- Backend runs on `http://localhost:8080` (configurable via `NEXT_PUBLIC_API_URL`)
- API client includes credentials for cookie-based authentication
- CORS is configured in the backend to allow requests from this frontend

## Deployment

The frontend can be deployed to:
- **Vercel** (recommended for Next.js)
- **Netlify**
- **AWS Amplify**
- Any platform supporting Node.js

Ensure environment variables are configured in the deployment platform.
