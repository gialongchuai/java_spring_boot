import { Link } from "react-router-dom";
import { useForm } from "react-hook-form";
import { useMutation } from "@tanstack/react-query";
import { authenticate } from "../../apis/auth.api";

export default function Register() {
  // const {
  //   register,
  //   handleSubmit,
  //   formState: { errors },
  // } = useForm();

  const { register, handleSubmit } = useForm<{
    username: string;
    password: string;
  }>();

  const authMutation = useMutation({
    mutationFn: authenticate,
  });

  const onSubmit = handleSubmit((data) => {
    authMutation.mutate(data); // ← data đã đúng shape
  });

  return (
    <div className="bg-orange">
      <div>
        <title>Đăng ký | Shopee Clone</title>
        <meta
          name="description"
          content="Đăng ký tài khoản vào dự án Shopee Clone"
        />
      </div>
      <div className="container">
        <div className="grid grid-cols-1 py-12 lg:grid-cols-5 lg:py-32 lg:pr-10">
          <div className="lg:col-span-2 lg:col-start-4">
            <form
              className="rounded bg-white p-10 shadow-sm"
              noValidate
              onSubmit={onSubmit}
            >
              <div className="text-2xl">Đăng ký</div>
              <input
                // type="email"
                className="mt-8"
                placeholder="Email"
                {...register("username")}
              />
              <input
                type="password"
                className="mt-2"
                placeholder="Password"
                autoComplete="on"
                {...register("password")}
              />

              {/* <input
                type="password"
                className="mt-2"
                placeholder="Confirm Password"
                autoComplete="on"
                {...register('confirm_password')}
              /> */}

              <div className="mt-2">
                <button
                  type="submit"
                  className="flex w-full items-center justify-center bg-red-500 py-4 px-2 text-sm uppercase text-white hover:bg-red-600"
                >
                  Đăng ký
                </button>
              </div>
              <div className="mt-8 flex items-center justify-center">
                <span className="text-gray-400">Bạn đã có tài khoản?</span>
                <Link className="ml-1 text-red-400" to="/login">
                  Đăng nhập
                </Link>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}
