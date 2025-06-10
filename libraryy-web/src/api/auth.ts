import http, { Response } from "@/lib/http";

export const registerAuth = async (
  data: UserRegister
): Promise<Response<null>> => {
  return await http.post("auth/register", { json: data }).json();
};

export const loginAuth = async (
  data: UserLogin
): Promise<Response<{ token: string; user: UserInfo }>> => {
  return await http
    .post("auth/login", {
      json: { ...data },
    })
    .json();
};
