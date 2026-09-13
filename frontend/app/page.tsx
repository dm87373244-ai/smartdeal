"use client";

import { useState } from "react";

export default function Home() {
  const [search, setSearch] = useState("");
  const [products, setProducts] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSearch = async () => {
    if (!search.trim()) return;

    setLoading(true);
    setError("");
    setProducts([]);

    try {
      const response = await fetch(
        `http://localhost:8080/api/products/search/${encodeURIComponent(search)}`
      );

      if (!response.ok) {
        throw new Error("Failed to fetch products");
      }

      const data = await response.json();
      setProducts(data);
    } catch (err) {
      setError("Backend se products fetch nahi ho pa rahe hain.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="min-h-screen bg-gray-50 text-gray-900">
      <nav className="bg-blue-600 text-white">
        <div className="mx-auto flex max-w-6xl items-center justify-between px-6 py-5">
          <h1 className="text-2xl font-bold">SmartDeal</h1>
          <span className="text-sm">Find the Best Deal</span>
        </div>
      </nav>

      <section className="mx-auto max-w-6xl px-6 py-20 text-center">
        <h2 className="text-5xl font-bold">
          Find the Best Product at the Best Price
        </h2>

        <p className="mt-5 text-lg text-gray-600">
          Compare products from Amazon, Flipkart & Meesho
        </p>

        <div className="mx-auto mt-10 flex max-w-2xl">
          <input
            type="text"
            placeholder="Search for a product..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                handleSearch();
              }
            }}
            className="flex-1 rounded-l-lg border border-gray-300 bg-white px-5 py-4 outline-none"
          />

          <button
            onClick={handleSearch}
            disabled={loading}
            className="rounded-r-lg bg-blue-600 px-8 py-4 font-semibold text-white hover:bg-blue-700 disabled:opacity-50"
          >
            {loading ? "Searching..." : "Search"}
          </button>
        </div>
      </section>

      {error && (
        <div className="mx-auto max-w-2xl px-6 pb-8 text-center text-red-600">
          {error}
        </div>
      )}

      {products.length > 0 && (
        <section className="mx-auto max-w-6xl px-6 pb-20">
          <h2 className="mb-8 text-3xl font-bold">Search Results</h2>

          <div className="grid gap-6 md:grid-cols-3">
            {products.map((product) => (
              <div
                key={product.id}
                className="rounded-xl bg-white p-6 shadow hover:shadow-lg"
              >
                <h3 className="text-xl font-bold">{product.name}</h3>

                <p className="mt-3 text-gray-600">
                  Platform: {product.marketplace}
                </p>

                <p className="mt-2 text-2xl font-bold text-green-600">
                  ₹{product.price}
                </p>

                <p className="mt-2">⭐ Rating: {product.rating}</p>

                {product.productUrl && (
                  <a
                    href={product.productUrl}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="mt-5 inline-block rounded-lg bg-blue-600 px-5 py-3 font-semibold text-white hover:bg-blue-700"
                  >
                    View Deal
                  </a>
                )}
              </div>
            ))}
          </div>
        </section>
      )}

      <section className="mx-auto max-w-6xl px-6 pb-20">
        <h2 className="mb-10 text-center text-3xl font-bold">
          Why SmartDeal?
        </h2>

        <div className="grid gap-6 md:grid-cols-3">
          <div className="rounded-xl bg-white p-7 shadow">
            <h3 className="text-xl font-bold">⭐ Quality Filter</h3>
            <p className="mt-3 text-gray-600">
              We show products with a rating of 3.5 or higher.
            </p>
          </div>

          <div className="rounded-xl bg-white p-7 shadow">
            <h3 className="text-xl font-bold">💰 Price Comparison</h3>
            <p className="mt-3 text-gray-600">
              Compare prices and find the cheapest deal.
            </p>
          </div>

          <div className="rounded-xl bg-white p-7 shadow">
            <h3 className="text-xl font-bold">🛒 Multiple Marketplaces</h3>
            <p className="mt-3 text-gray-600">
              Compare Amazon, Flipkart and Meesho in one place.
            </p>
          </div>
        </div>
      </section>

      <footer className="border-t bg-white py-6 text-center text-gray-500">
        © 2026 SmartDeal. Compare smarter, buy better.
      </footer>
    </main>
  );
}
