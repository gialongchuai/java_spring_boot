import type { DataLog } from './user.type'
import type { SuccessResponse } from './utils.type'

export type AuthResponse = SuccessResponse<{
  message: string
  status: string
  data: DataLog
}>

// export type RefreshTokenReponse = SuccessResponse<{ access_token: string }>
