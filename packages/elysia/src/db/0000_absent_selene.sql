-- Current sql file was generated after introspecting the database
-- If you want to run this migration please uncomment this code before executing migrations
/*
CREATE TYPE "public"."role" AS ENUM('Admin', 'Common');--> statement-breakpoint
CREATE TABLE "pgmigrations" (
	"id" serial PRIMARY KEY NOT NULL,
	"name" varchar(255) NOT NULL,
	"run_on" timestamp NOT NULL
);
--> statement-breakpoint
CREATE TABLE "users" (
	"id" uuid PRIMARY KEY DEFAULT uuidv7() NOT NULL,
	"account" text NOT NULL,
	"hashed_password" text NOT NULL,
	"name" text NOT NULL,
	"role" "role" DEFAULT 'Common' NOT NULL,
	"disabled_at" timestamp with time zone,
	CONSTRAINT "users_unique" UNIQUE("account")
);
--> statement-breakpoint
CREATE TABLE "todos" (
	"id" uuid PRIMARY KEY DEFAULT uuidv7() NOT NULL,
	"creator_id" uuid NOT NULL,
	"assignee_id" uuid NOT NULL,
	"title" text NOT NULL,
	"memo" text,
	"due_date" date,
	"completed_at" timestamp with time zone,
	"created_at" timestamp with time zone DEFAULT now() NOT NULL,
	"updated_at" timestamp with time zone DEFAULT now() NOT NULL
);
--> statement-breakpoint
ALTER TABLE "todos" ADD CONSTRAINT "todos_creator_id_fk" FOREIGN KEY ("creator_id") REFERENCES "public"."users"("id") ON DELETE cascade ON UPDATE no action;--> statement-breakpoint
ALTER TABLE "todos" ADD CONSTRAINT "todos_assignee_id_fk" FOREIGN KEY ("assignee_id") REFERENCES "public"."users"("id") ON DELETE cascade ON UPDATE no action;
*/