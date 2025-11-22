# Spring Bootプロジェクト ファイル解説

このドキュメントは、`packages/spring-boot/src` 配下にあるSpring Bootプロジェクトの各ファイルの役割について解説します。

## プロジェクト構成

```
src
├── main
│   ├── java
│   │   └── jp/co/itfllc/WebSystemSamples
│   │       ├── WebMvcConfig.java                 # Web MVC設定
│   │       ├── WebSystemSamplesApplication.java  # Spring Bootアプリケーションのエントリーポイント
│   │       ├── advices
│   │       │   └── GlobalExceptionHandler.java   # グローバルな例外ハンドリング
│   │       ├── enums
│   │       │   └── Role.java                     # ユーザーロールのEnum
│   │       ├── features
│   │       │   ├── login                         # ログイン機能
│   │       │   │   ├── LoginController.java
│   │       │   │   └── LoginService.java
│   │       │   └── masters
│   │       │       └── users                     # ユーザー管理機能
│   │       │           ├── UsersController.java
│   │       │           └── UsersService.java
│   │       ├── interceptors
│   │       │   ├── AdminInterceptor.java         # 管理者権限チェックインターセプター
│   │       │   └── AuthInterceptor.java          # 認証インターセプター
│   │       ├── mappers
│   │       │   ├── TodosMapper.java              # Todosテーブル(MyBatis)
│   │       │   ├── UsersMapper.java              # Usersテーブル(MyBatis)
│   │       │   └── results
│   │       │       ├── TodosResult.java
│   │       │       └── entities
│   │       │           ├── TodosEntity.java
│   │       │           └── UsersEntity.java
│   │       └── utils
│   │           ├── CryptoUtils.java              # パスワード暗号化ユーティリティ
│   │           └── JwtUtils.java                 # JWT生成・検証ユーティリティ
│   └── resources
│       ├── application.properties              # アプリケーション設定
│       ├── mappers
│       │   ├── TodosMapper.xml                 # TodosMapperのSQL
│       │   └── UsersMapper.xml                 # UsersMapperのSQL
│       └── META-INF
│           └── additional-spring-configuration-metadata.json
└── test
    └── java
        └── ... (テストコード)
```

## 主要ファイル解説

### 1. アプリケーションエントリーポイント

- **`WebSystemSamplesApplication.java`**: Spring Bootアプリケーションを起動する`main`メソッドが含まれるクラスです。`@SpringBootApplication`アノテーションにより、自動設定、コンポーネントスキャンなどが行われます。

### 2. 設定

- **`WebMvcConfig.java`**: Web MVCに関する設定を行うクラスです。このプロジェクトでは、後述する`AuthInterceptor`と`AdminInterceptor`をインターセプターとして登録しています。
- **`application.properties`**: アプリケーションの各種設定を定義するファイルです。データベース接続情報、MyBatisの設定、JWT関連の秘密鍵や有効期限などが環境変数経由で設定されています。

### 3. 機能 (Features)

#### ログイン機能 (`features/login`)

- **`LoginController.java`**: `/login`パスへのPOSTリクエストを処理します。`LoginService`を呼び出して認証を行い、成功した場合はJWT（アクセストークンとリフレッシュトークン）を返却します。
- **`LoginService.java`**: 実際のログイン認証ロジックを担当します。DBからユーザー情報を取得し、パスワードを検証します。

#### ユーザー管理機能 (`features/masters/users`)

- **`UsersController.java`**: `/masters/users`パスへのGETリクエストを処理し、ユーザーの一覧を返却します。
- **`UsersService.java`**: `UsersMapper`を使ってデータベースからユーザーリストを取得します。

### 4. インターセプター (Interceptors)

- **`AuthInterceptor.java`**: `/login`を除くすべてのパスでリクエストをインターセプトします。リクエストヘッダーの`Authorization: Bearer <token>`からJWTを検証し、有効な場合はリクエスト属性にユーザー情報をセットします。認証に失敗した場合は`401 Unauthorized`エラーを返します。
- **`AdminInterceptor.java`**: `/masters/**`のパスでリクエストをインターセプトします。`AuthInterceptor`によってセットされたユーザー情報をもとに、ユーザーが`Admin`ロールを持っているかを確認します。管理者でない場合は`403 Forbidden`エラーを返します。

### 5. データアクセス (Mappers)

MyBatisを利用してデータベースにアクセスします。

- **`UsersMapper.java` / `UsersMapper.xml`**: `users`テーブルへのCRUD操作を定義します。
- **`TodosMapper.java` / `TodosMapper.xml`**: `todos`テーブルへの操作を定義します。

### 6. エンティティとEnum

- **`UsersEntity.java`**: `users`テーブルのレコードに対応するJavaオブジェクトです。
- **`TodosEntity.java`**: `todos`テーブルのレコードに対応するJavaオブジェクトです。
- **`Role.java`**: `Admin`と`Common`の2つのユーザーロールを定義したEnumです。

### 7. ユーティリティ (Utils)

- **`CryptoUtils.java`**: パスワードのハッシュ化（Argon2）と検証を行うためのユーティリティクラスです。
- **`JwtUtils.java`**: RSA暗号方式（RS256）を用いてJWTの生成と検証を行います。秘密鍵で署名し、公開鍵で検証します。`application.properties`から鍵文字列や発行者、有効期限を読み込みます。

### 8. 例外処理 (Advices)

- **`GlobalExceptionHandler.java`**: `@RestControllerAdvice`アノテーションにより、アプリケーション全体で発生した例外を捕捉し、統一された形式のエラーレスポンスを返却します。`ResponseStatusException`やその他の一般的な`Exception`をハンドリングします。
