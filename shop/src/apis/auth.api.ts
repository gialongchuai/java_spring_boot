import type { AuthResponse } from "../types/auth.type";
import http from "../utils/http";

export const login = (body: { username: string; password: string }) =>
  http.post<AuthResponse>("/auth/access", body);
