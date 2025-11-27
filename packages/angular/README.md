# WebSystemSamples (Angular)

このプロジェクトは[Angular CLI](https://github.com/angular/angular-cli)を使用して生成されたAngularフロントエンドアプリケーションです。

## 推奨されるパッケージマネージャー

このプロジェクトでは、パッケージマネージャーとして`bun`の使用が推奨されています。`package.json`の`scripts`セクションに定義されたコマンドは、`bun run <script-name>`を使用して実行してください。

## 開発

### 開発サーバーの起動

ローカル開発サーバーを起動するには、以下のコマンドを実行します。

```bash
bun run start
```

このコマンドは`ng serve --host 0.0.0.0`を実行します。サーバーが起動したら、ブラウザで `http://localhost:4200/` を開いてください。ソースファイルを変更すると、アプリケーションは自動的にリロードされます。

### APIクライアントの自動生成

開発サーバーの起動前に`orval`が実行され、OpenAPIスキーマ（`../spring-boot/schema.json`）からAPIクライアントコードを自動生成します。これにより、バックエンドの変更がフロントエンドの型定義に即座に反映されます。

## コードの生成

Angular CLIを使用して、新しいコンポーネントやその他のファイルを生成できます。

```bash
ng generate component component-name
```

利用可能なスキーマティックス（例：`components`, `directives`, `pipes`）の一覧は、以下のコマンドで確認できます。

```bash
ng generate --help
```

## ビルド

プロジェクトを本番用にビルドするには、以下のコマンドを実行します。

```bash
bun run build
```

これにより、プロジェクトがコンパイルされ、ビルド成果物が`dist/`ディレクトリに保存されます。

**ビルド後の処理について:**
`build`コマンドの実行後、`postbuild`スクリプトが`orval`をデフォルト設定で実行します。これは、ビルド完了後に開発者がすぐにローカル開発に戻れるように、`src/app/shared/api/`内の生成されたAPIクライアントのソースファイルを開発環境の`baseUrl`（`http://localhost:56080/api/`）に再設定するためのものです。この処理は`dist/`ディレクトリ内のビルド成果物には影響しません。

## テストとリント

### 単体テスト

このプロジェクトでは、テストフレームワークとして`vitest`を使用しています。単体テストを実行するには、以下のコマンドを使用します。

```bash
bun run test
```

### コードの静的解析 (Lint)

ESLintを使用してコードの品質をチェックするには、以下のコマンドを実行します。

```bash
bun run lint
```

## その他のリソース

Angular CLIの詳細については、[Angular CLI Overview and Command Reference](https://angular.dev/tools/cli)の公式ドキュメントを参照してください。
