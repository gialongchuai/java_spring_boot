import { yupResolver } from "@hookform/resolvers/yup";
import { useMutation } from "@tanstack/react-query";
import { useForm } from "react-hook-form";
import { Link } from "react-router-dom";
import { login } from "../../apis/auth.api";
import Input from "../../components/Input/Input";
import { schema, type Schema } from "../../utils/rules";

type FormData = Pick<Schema, "username" | "password">;
const loginSchema = schema.pick(["username", "password"]);

export default function Login() {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<FormData>({
    resolver: yupResolver(loginSchema)
  });

  // const onSubmit = handleSubmit((data) => console.log(data));

  const loginMutation = useMutation({
    mutationFn: login,
  });

  const onSubmit = handleSubmit((data) => {
    loginMutation.mutate(data, {
      onSuccess: (data) => {
        console.log(data);
      }
    })
  });

  return (
    <div className="bg-purple-300">
      <div>
        <title>Đăng nhập | Shopee Clone</title>
        <meta name="description" content="Đăng nhập vào dự án Shopee Clone" />
      </div>
      <div className="container">
        <div className="grid grid-cols-1 py-12 lg:grid-cols-5 lg:py-32 lg:pr-10">
          <div className="lg:col-span-2 lg:col-start-4">
            <form
              onSubmit={onSubmit}
              className="rounded bg-white p-10 shadow-sm"
              noValidate
            >
              <div className="text-2xl">Đăng nhập</div>

              <Input
                name="username"
                register={register}
                type="username"
                className="mt-8"
                errorMessage={errors.username?.message}
                placeholder="Username"
              />

              <Input
                name="password"
                register={register}
                type="password"
                className="mt-2"
                errorMessage={errors.password?.message}
                placeholder="Password"
              />

              <div className="mt-3">
                <button
                  type="submit"
                  className="flex  w-full items-center justify-center bg-red-500 py-4 px-2 text-sm uppercase text-white hover:bg-red-600"
                >
                  Đăng nhập
                </button>
              </div>
              <div className="mt-8 flex items-center justify-center">
                <span className="text-gray-400">Bạn chưa có tài khoản?</span>
                <Link className="ml-1 text-red-400" to="/register">
                  Đăng ký
                </Link>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}
