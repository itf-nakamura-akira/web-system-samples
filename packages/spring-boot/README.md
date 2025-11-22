# Spring Boot APIサーバー (`spring-boot`)

このパッケージは、Spring Bootで構築されたWeb APIサーバーです。

## 概要

REST APIを提供し、データベースパッケージと連携してデータの永続化を行います。
認証機能にはJWT（JSON Web Token）を使用しています。

詳細なプロジェクトの構成については、[`src/README.md`](./src/README.md) を参照してください。

## 主なコマンド

### 依存関係のインストール

Mavenの依存関係をダウンロードします。

```bash
./mvnw dependency:resolve
```

### アプリケーションのビルド

プロジェクトをビルドして、`target`ディレクトリにjarファイルを生成します。

```bash
./mvnw clean package
```

### アプリケーションの実行

Spring Bootアプリケーションを起動します。
起動後、APIは `http://localhost:8080/api` で利用可能になります。

```bash
./mvnw spring-boot:run
```
