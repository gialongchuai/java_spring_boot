import http from "../utils/http";

export const authenticate = (body: {username: string; password: string}) => http.post('/auth/access',body);