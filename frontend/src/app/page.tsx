export default function Home() {
  return (
    <main className="min-h-screen flex items-center justify-center p-8">
      <div className="max-w-2xl text-center">
        <h1 className="text-4xl font-bold mb-4">
          Welcome to DietMe
        </h1>
        <p className="text-lg text-gray-600 mb-8">
          Your diet mentoring platform connecting dietitians with their clients
        </p>
        <div className="flex gap-4 justify-center">
          <button className="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition">
            Get Started
          </button>
          <button className="px-6 py-3 border border-gray-300 rounded-lg hover:bg-gray-50 transition">
            Learn More
          </button>
        </div>
      </div>
    </main>
  )
}
